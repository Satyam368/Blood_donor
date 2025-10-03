
import { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Heart, User, Droplet, ArrowLeft } from "lucide-react";

const bloodTypes = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

const donorSchema = z
  .object({
    fullName: z.string().trim().min(1, "Full name is required").max(100),
    email: z.string().trim().min(1, "Email is required").email("Invalid email format"),
    phone: z
      .string()
      .trim()
      .min(1, "Phone is required")
      .regex(/^[+]?(?:\d[ -]?){7,15}\d$/, "Invalid phone number"),
    bloodType: z.enum(["A+","A-","B+","B-","AB+","AB-","O+","O-"], { required_error: "Blood type is required" }),
    dateOfBirth: z
      .string()
      .trim()
      .min(1, "Date of birth is required")
      .refine((v) => !Number.isNaN(Date.parse(v)), "Invalid date")
      .refine((v) => {
        const dob = new Date(v);
        const today = new Date();
        const age = today.getFullYear() - dob.getFullYear() - (today < new Date(today.getFullYear(), dob.getMonth(), dob.getDate()) ? 1 : 0);
        return age >= 18;
      }, "You must be at least 18 years old"),
    address: z.string().trim().min(1, "Address is required"),
    city: z.string().trim().min(1, "City is required"),
    state: z.string().trim().min(1, "State is required"),
    zipCode: z
      .string()
      .trim()
      .min(1, "ZIP code is required")
      .regex(/^[0-9A-Za-z -]{3,10}$/, "Invalid ZIP/Postal code"),
    weight: z
      .string()
      .trim()
      .min(1, "Weight is required")
      .refine((v) => !Number.isNaN(Number(v)), "Weight must be a number")
      .refine((v) => Number(v) >= 45, "Minimum eligible weight is 45 kg")
      .refine((v) => Number(v) <= 200, "Weight must be realistic (<= 200 kg)"),
    medicalConditions: z.string().optional().default(""),
    lastDonation: z.string().optional().default("")
      .refine((v) => !v || !Number.isNaN(Date.parse(v)), "Invalid date"),
    emergencyContact: z.string().trim().min(1, "Emergency contact is required"),
    emergencyPhone: z
      .string()
      .trim()
      .min(1, "Emergency phone is required")
      .regex(/^[+]?(?:\d[ -]?){7,15}\d$/, "Invalid phone number"),
    agreeToTerms: z.boolean().refine(v => v, { message: "You must agree to the terms" }),
    availableForEmergency: z.boolean().optional().default(false)
  })
  .superRefine((data, ctx) => {
    if (data.lastDonation) {
      const last = new Date(data.lastDonation as string);
      const today = new Date();
      if (Number.isNaN(last.getTime())) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, path: ["lastDonation"], message: "Invalid date" });
      } else {
        if (last > today) {
          ctx.addIssue({ code: z.ZodIssueCode.custom, path: ["lastDonation"], message: "Last donation date cannot be in the future" });
        }
        const dob = new Date(data.dateOfBirth as string);
        if (!Number.isNaN(dob.getTime()) && last < dob) {
          ctx.addIssue({ code: z.ZodIssueCode.custom, path: ["lastDonation"], message: "Last donation cannot be before your birth date" });
        }
      }
    }
  });

type DonorForm = z.infer<typeof donorSchema>;

const BecomeDonor = () => {

  const [message, setMessage] = useState("");

  const { register, control, handleSubmit: rhfHandleSubmit, formState: { errors }, setError, reset, watch } = useForm<DonorForm>({
    resolver: zodResolver(donorSchema),
    defaultValues: {
      agreeToTerms: false,
      availableForEmergency: false
    }
  });

  const onSubmit = async (values: DonorForm) => {
    try {
      console.log("🔍 Become Donor attempt:", values.fullName);
      
      // First test if backend is accessible
      console.log("🧪 Testing backend connectivity...");
      try {
        const testResponse = await fetch("http://localhost:8081/api/donors", {
          method: "GET",
          headers: { "Content-Type": "application/json" },
        });
        console.log("✅ Backend test response status:", testResponse.status);
      } catch (testError) {
        console.error("❌ Backend connectivity test failed:", testError);
        setMessage("⚠️ Cannot connect to backend server. Please make sure it's running on port 8081.");
        return;
      }
      
      console.log("📡 Sending donor registration request to:", "http://localhost:8081/api/donors");
      console.log("📤 Request payload:", {
        ...values,
        weight: values.weight ? Number(values.weight) : null,
        lastDonation: values.lastDonation || null
      });
      
      const response = await fetch("http://localhost:8081/api/donors", {
        method: "POST",
        headers: { 
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        body: JSON.stringify({
          ...values,
          weight: values.weight ? Number(values.weight) : null,
          lastDonation: values.lastDonation || null
        }),
      });

      console.log("📊 Response status:", response.status);
      console.log("📋 Response headers:", Object.fromEntries(response.headers.entries()));

      if (response.ok) {
        setMessage("🎉 Donor registered successfully!");
        // Reset both RHF state and local mirror state
        reset({
          fullName: "",
          email: "",
          phone: "",
          dateOfBirth: "",
          address: "",
          city: "",
          state: "",
          zipCode: "",
          weight: "",
          medicalConditions: "",
          lastDonation: "",
          emergencyContact: "",
          emergencyPhone: "",
          agreeToTerms: false,
          availableForEmergency: false
        });
      } else {
        // Try to map backend validation errors
        const data = await response.json().catch(() => null);
        if (data && data.fields) {
          Object.entries<string>(data.fields).forEach(([field, msg]) => {
            // @ts-ignore
            setError(field as keyof DonorForm, { type: "server", message: msg });
          });
          setMessage("⚠️ Please fix the highlighted errors.");
        } else {
          setMessage("⚠️ Failed to register donor. Please try again.");
        }
      }
    } catch (error) {
      console.error(error);
      setMessage("❌ Error connecting to the server.");
    }
  };

  return (
    <div className="min-h-screen bg-background">
      <nav className="border-b border-border bg-card/50 backdrop-blur-sm sticky top-0 z-50">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <Heart className="h-8 w-8 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">BloodConnect</h1>
            </div>
            <div className="flex items-center space-x-4">
              <Button variant="ghost" asChild>
                <Link to="/">
                  <ArrowLeft className="mr-2 h-4 w-4" />
                  Back to Home
                </Link>
              </Button>
              <Button variant="outline" asChild>
                <Link to="/login">Sign In</Link>
              </Button>
            </div>
          </div>
        </div>
      </nav>

      <div className="container mx-auto px-4 py-8">
        <div className="max-w-4xl mx-auto">
          <div className="text-center mb-8">
            <h2 className="text-4xl font-bold mb-4">Become a Life Saver</h2>
            <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
              Join thousands of heroes making a difference. Your blood donation can save up to three lives.
            </p>
          </div>

          <Card className="shadow-card">
            <CardHeader>
              <CardTitle className="flex items-center">
                <User className="mr-2 h-5 w-5 text-primary" />
                Donor Registration
              </CardTitle>
              <CardDescription>
                Please fill out this form to register as a blood donor. All information is kept confidential.
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">

              {/* Personal Info */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Personal Information</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">Full Name *</label>
                    <Input placeholder="Enter full name" {...register("fullName")} />
                    {errors.fullName && <p className="text-sm text-red-600 mt-1">{errors.fullName.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Email *</label>
                    <Input type="email" placeholder="Enter email" {...register("email")} />
                    {errors.email && <p className="text-sm text-red-600 mt-1">{errors.email.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Phone *</label>
                    <Input type="tel" placeholder="Enter phone" {...register("phone")} />
                    {errors.phone && <p className="text-sm text-red-600 mt-1">{errors.phone.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Date of Birth *</label>
                    <Input type="date" {...register("dateOfBirth")} />
                    {errors.dateOfBirth && <p className="text-sm text-red-600 mt-1">{errors.dateOfBirth.message}</p>}
                  </div>
                </div>
              </div>

              {/* Medical Info */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Medical Information</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">Blood Type *</label>
                    <Controller
                      name="bloodType"
                      control={control}
                      render={({ field }) => (
                        <Select value={field.value} onValueChange={(value) => field.onChange(value)}>
                          <SelectTrigger><SelectValue placeholder="Select blood type" /></SelectTrigger>
                          <SelectContent>
                            {bloodTypes.map(type => (
                              <SelectItem key={type} value={type}>
                                <div className="flex items-center">
                                  <Droplet className="mr-2 h-4 w-4 text-primary" />{type}
                                </div>
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                      )}
                    />
                    {errors.bloodType && <p className="text-sm text-red-600 mt-1">{errors.bloodType.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Weight (kg) *</label>
                    <Input type="number" placeholder="Enter weight" {...register("weight")} />
                    {errors.weight && <p className="text-sm text-red-600 mt-1">{errors.weight.message}</p>}
                  </div>
                  <div className="md:col-span-2">
                    <label className="block text-sm font-medium mb-2">Medical Conditions</label>
                    <Input placeholder="List medical conditions" {...register("medicalConditions")} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Last Donation Date</label>
                    <Input type="date" {...register("lastDonation")} />
                  </div>
                </div>
              </div>

              {/* Address Info */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Address</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="md:col-span-2">
                    <label className="block text-sm font-medium mb-2">Street Address *</label>
                    <Input placeholder="Enter address" {...register("address")} />
                    {errors.address && <p className="text-sm text-red-600 mt-1">{errors.address.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">City *</label>
                    <Input placeholder="Enter city" {...register("city")} />
                    {errors.city && <p className="text-sm text-red-600 mt-1">{errors.city.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">State *</label>
                    <Input placeholder="Enter state" {...register("state")} />
                    {errors.state && <p className="text-sm text-red-600 mt-1">{errors.state.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">ZIP Code *</label>
                    <Input placeholder="Enter ZIP code" {...register("zipCode")} />
                    {errors.zipCode && <p className="text-sm text-red-600 mt-1">{errors.zipCode.message}</p>}
                  </div>
                </div>
              </div>

              {/* Emergency Contact */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Emergency Contact</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">Emergency Contact Name *</label>
                    <Input placeholder="Enter emergency contact name" {...register("emergencyContact")} />
                    {errors.emergencyContact && <p className="text-sm text-red-600 mt-1">{errors.emergencyContact.message}</p>}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Emergency Contact Phone *</label>
                    <Input placeholder="Enter phone" {...register("emergencyPhone")} />
                    {errors.emergencyPhone && <p className="text-sm text-red-600 mt-1">{errors.emergencyPhone.message}</p>}
                  </div>
                </div>
              </div>

              {/* Preferences */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Preferences</h3>
                <div className="space-y-4">
                  <div className="flex items-center space-x-2">
                    <Controller
                      name="availableForEmergency"
                      control={control}
                      render={({ field }) => (
                        <Checkbox id="emergency" checked={!!field.value} onCheckedChange={(checked) => field.onChange(!!checked)} />
                      )}
                    />
                    <label htmlFor="emergency" className="text-sm">Available for emergency donations (24/7)</label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Controller
                      name="agreeToTerms"
                      control={control}
                      render={({ field }) => (
                        <Checkbox id="terms" checked={!!field.value} onCheckedChange={(checked) => field.onChange(!!checked)} />
                      )}
                    />
                    <label htmlFor="terms" className="text-sm">Agree to terms & privacy policy *</label>
                  </div>
                  {errors.agreeToTerms && <p className="text-sm text-red-600 mt-1">{errors.agreeToTerms.message}</p>}
                </div>
              </div>

              {/* Submit */}
              <div className="flex flex-col items-center pt-6 space-y-4">
                <Button variant="hero" size="lg" className="w-full md:w-auto" disabled={!watch("agreeToTerms")} onClick={rhfHandleSubmit(onSubmit)}>
                  <Heart className="mr-2 h-5 w-5" /> Register as Blood Donor
                </Button>
                {message && <p className="text-center text-sm text-muted-foreground">{message}</p>}
              </div>

            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default BecomeDonor;
