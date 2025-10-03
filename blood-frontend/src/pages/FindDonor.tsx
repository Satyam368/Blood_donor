import { useState, useEffect, useMemo } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Heart, Search, MapPin, Phone, Droplet, Clock, ArrowLeft } from "lucide-react";

type Donor = {
  id: number;
  fullName: string;
  email?: string;
  phone?: string;
  bloodType: string;
  dateOfBirth?: string;
  weight?: number;
  medicalConditions?: string;
  lastDonation?: string;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  emergencyContact?: string;
  emergencyPhone?: string;
  agreeToTerms?: boolean;
  availableForEmergency?: boolean;
};

const bloodTypes = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8081";

const FindDonor = () => {
  const [searchFilters, setSearchFilters] = useState<{ bloodType: string; city: string }>({
    bloodType: "",
    city: "",
  });
  const [donors, setDonors] = useState<Donor[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const paramsString = useMemo(() => {
    const params = new URLSearchParams();
    if (searchFilters.city) params.append("city", searchFilters.city);
    if (searchFilters.bloodType) params.append("bloodType", searchFilters.bloodType);
    return params.toString();
  }, [searchFilters.city, searchFilters.bloodType]);

  const handleFilterChange = (field: "bloodType" | "city", value: string) => {
    setSearchFilters((prev) => ({ ...prev, [field]: value }));
  };

  const fetchDonors = async () => {
    try {
      setLoading(true);
      setError(null);
      const url = `${API_BASE}/api/find-donors/search${paramsString ? `?${paramsString}` : ""}`;
      const res = await fetch(url);
      if (!res.ok) throw new Error(await res.text());
      const data: Donor[] = await res.json();
      setDonors(data);
    } catch (e: any) {
      setError(e?.message || "Failed to fetch donors");
      setDonors([]);
    } finally {
      setLoading(false);
    }
  };

  // Initial fetch and debounced refetch on filters change
  useEffect(() => {
    const t = setTimeout(() => {
      void fetchDonors();
    }, 300);
    return () => clearTimeout(t);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [paramsString]);

  return (
    <div className="min-h-screen bg-background">
      {/* Navigation */}
      <nav className="border-b border-border bg-card/50 backdrop-blur-sm sticky top-0 z-50">
        <div className="container mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Heart className="h-8 w-8 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">BloodConnect</h1>
          </div>
          <div className="flex items-center space-x-4">
            <Button variant="ghost" asChild>
              <Link to="/">
                <ArrowLeft className="mr-2 h-4 w-4" /> Back to Home
              </Link>
            </Button>
          </div>
        </div>
      </nav>

      {/* Header */}
      <section className="py-12 bg-gradient-subtle text-center">
        <h2 className="text-4xl font-bold mb-4">Find Blood Donors</h2>
        <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
          Connect with verified blood donors in your area. Search by blood type and city.
        </p>
      </section>

      {/* Search Filters */}
      <section className="py-8 bg-card/50">
        <div className="container mx-auto px-4">
          <Card className="shadow-card">
            <CardHeader>
              <CardTitle className="flex items-center">
                <Search className="mr-2 h-5 w-5 text-primary" /> Search Filters
              </CardTitle>
              <CardDescription>Refine your search to find the most suitable donors</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-2">Blood Type</label>
                  <Select
                    value={searchFilters.bloodType}
                    onValueChange={(value) => handleFilterChange("bloodType", value)}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select blood type" />
                    </SelectTrigger>
                    <SelectContent>
                      {bloodTypes.map((type) => (
                        <SelectItem key={type} value={type}>
                          <div className="flex items-center">
                            <Droplet className="mr-2 h-4 w-4 text-primary" />
                            {type}
                          </div>
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>

                <div>
                  <label className="block text-sm font-medium mb-2">City</label>
                  <Input
                    placeholder="Enter city"
                    value={searchFilters.city}
                    onChange={(e) => handleFilterChange("city", e.target.value)}
                  />
                </div>

                <div className="flex items-end">
                  <Button variant="hero" className="w-full" onClick={fetchDonors} disabled={loading}>
                    <Search className="mr-2 h-4 w-4" /> {loading ? "Searching..." : "Search Donors"}
                  </Button>
                </div>
              </div>
              {error && <p className="text-sm text-red-500 mt-3">{error}</p>}
            </CardContent>
          </Card>
        </div>
      </section>

      {/* Donors List */}
      <section className="py-8">
        <div className="container mx-auto px-4">
          <h3 className="text-2xl font-bold mb-4">Available Donors</h3>
          {loading && donors.length === 0 ? (
            <p className="text-muted-foreground">Loading donors...</p>
          ) : donors.length === 0 ? (
            <p className="text-muted-foreground">No donors found for the selected filters.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {donors.map((donor) => (
                <Card key={donor.id} className="shadow-card hover:shadow-medical transition-all">
                  <CardHeader>
                    <div className="flex items-start justify-between">
                      <div>
                        <CardTitle className="text-lg">{donor.fullName}</CardTitle>
                        <CardDescription className="flex items-center mt-1">
                          <MapPin className="mr-1 h-4 w-4" />
                          {donor.city || "N/A"}
                          {donor.state ? `, ${donor.state}` : ""}
                        </CardDescription>
                      </div>
                      <div className="text-center">
                        <div className="w-12 h-12 bg-gradient-primary rounded-full flex items-center justify-center">
                          <span className="text-primary-foreground font-bold">{donor.bloodType}</span>
                        </div>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent className="space-y-2">
                    <div className="flex justify-between items-center">
                      <span className="text-sm text-muted-foreground">Last Donation:</span>
                      <span className="font-medium">{donor.lastDonation || "N/A"}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm text-muted-foreground">Emergency Contact:</span>
                      <span className="font-medium">{donor.emergencyContact || "N/A"}</span>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {donor.availableForEmergency && (
                        <Badge className="bg-accent text-accent-foreground">
                          <Clock className="mr-1 h-3 w-3" /> Available
                        </Badge>
                      )}
                      {donor.agreeToTerms && (
                        <Badge className="bg-warning text-warning-foreground">Verified</Badge>
                      )}
                    </div>
                    <Button asChild variant="hero" size="sm" className="w-full flex items-center justify-center mt-2">
                      <a href={`tel:${donor.phone || donor.emergencyPhone || ""}`}>
                        <Phone className="mr-2 h-4 w-4" /> Contact
                      </a>
                    </Button>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </section>
    </div>
  );
};

export default FindDonor;