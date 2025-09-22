
import { useState } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Heart, User, Droplet, ArrowLeft } from "lucide-react";

const bloodTypes = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

const BecomeDonor = () => {
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
    bloodType: "",
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

  const [message, setMessage] = useState("");

  const handleInputChange = (field: string, value: string | boolean) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/donors", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setMessage("🎉 Donor registered successfully!");
        setFormData({
          fullName: "",
          email: "",
          phone: "",
          bloodType: "",
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
        setMessage("⚠️ Failed to register donor. Please try again.");
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
                    <Input placeholder="Enter full name" value={formData.fullName} onChange={(e) => handleInputChange("fullName", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Email *</label>
                    <Input type="email" placeholder="Enter email" value={formData.email} onChange={(e) => handleInputChange("email", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Phone *</label>
                    <Input type="tel" placeholder="Enter phone" value={formData.phone} onChange={(e) => handleInputChange("phone", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Date of Birth *</label>
                    <Input type="date" value={formData.dateOfBirth} onChange={(e) => handleInputChange("dateOfBirth", e.target.value)} />
                  </div>
                </div>
              </div>

              {/* Medical Info */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Medical Information</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">Blood Type *</label>
                    <Select value={formData.bloodType} onValueChange={(value) => handleInputChange("bloodType", value)}>
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
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Weight (kg) *</label>
                    <Input type="number" placeholder="Enter weight" value={formData.weight} onChange={(e) => handleInputChange("weight", e.target.value)} />
                  </div>
                  <div className="md:col-span-2">
                    <label className="block text-sm font-medium mb-2">Medical Conditions</label>
                    <Input placeholder="List medical conditions" value={formData.medicalConditions} onChange={(e) => handleInputChange("medicalConditions", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Last Donation Date</label>
                    <Input type="date" value={formData.lastDonation} onChange={(e) => handleInputChange("lastDonation", e.target.value)} />
                  </div>
                </div>
              </div>

              {/* Address Info */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Address</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="md:col-span-2">
                    <label className="block text-sm font-medium mb-2">Street Address *</label>
                    <Input placeholder="Enter address" value={formData.address} onChange={(e) => handleInputChange("address", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">City *</label>
                    <Input placeholder="Enter city" value={formData.city} onChange={(e) => handleInputChange("city", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">State *</label>
                    <Input placeholder="Enter state" value={formData.state} onChange={(e) => handleInputChange("state", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">ZIP Code *</label>
                    <Input placeholder="Enter ZIP code" value={formData.zipCode} onChange={(e) => handleInputChange("zipCode", e.target.value)} />
                  </div>
                </div>
              </div>

              {/* Emergency Contact */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Emergency Contact</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">Emergency Contact Name *</label>
                    <Input placeholder="Enter emergency contact name" value={formData.emergencyContact} onChange={(e) => handleInputChange("emergencyContact", e.target.value)} />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">Emergency Contact Phone *</label>
                    <Input placeholder="Enter phone" value={formData.emergencyPhone} onChange={(e) => handleInputChange("emergencyPhone", e.target.value)} />
                  </div>
                </div>
              </div>

              {/* Preferences */}
              <div>
                <h3 className="text-lg font-semibold mb-4">Preferences</h3>
                <div className="space-y-4">
                  <div className="flex items-center space-x-2">
                    <Checkbox id="emergency" checked={formData.availableForEmergency} onCheckedChange={(checked) => handleInputChange("availableForEmergency", checked as boolean)} />
                    <label htmlFor="emergency" className="text-sm">Available for emergency donations (24/7)</label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Checkbox id="terms" checked={formData.agreeToTerms} onCheckedChange={(checked) => handleInputChange("agreeToTerms", checked as boolean)} />
                    <label htmlFor="terms" className="text-sm">Agree to terms & privacy policy *</label>
                  </div>
                </div>
              </div>

              {/* Submit */}
              <div className="flex flex-col items-center pt-6 space-y-4">
                <Button variant="hero" size="lg" className="w-full md:w-auto" disabled={!formData.agreeToTerms} onClick={handleSubmit}>
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
