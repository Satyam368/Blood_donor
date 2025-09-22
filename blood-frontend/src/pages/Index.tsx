import { useState } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Heart, MapPin, Phone, Search, Users, Droplet, Clock, Shield } from "lucide-react";
import heroImage from "@/assets/hero-blood-donation.jpg";

const bloodTypes = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

const Index = () => {
  const [selectedBloodType, setSelectedBloodType] = useState("");
  const [location, setLocation] = useState("");

  return (
    <div className="min-h-screen bg-background">
      {/* Navigation */}
      <nav className="border-b border-border bg-card/50 backdrop-blur-sm sticky top-0 z-50">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <Heart className="h-8 w-8 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">BloodConnect</h1>
            </div>
            <div className="flex items-center space-x-4">
              <Button variant="ghost" asChild>
                <Link to="/find-donor">Find Donors</Link>
              </Button>
              <Button variant="ghost" asChild>
                <Link to="/become-donor">Become Donor</Link>
              </Button>
              <Button variant="ghost">About</Button>
              <Button variant="outline" asChild>
                <Link to="/login">Sign In</Link>
              </Button>
              <Button variant="hero" asChild>
                <Link to="/signup">Sign Up</Link>
              </Button>
              <Button variant="medical">Emergency Request</Button>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="relative py-20 overflow-hidden">
        <div className="absolute inset-0">
          <img 
            src={heroImage} 
            alt="Blood donation hero image"
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-hero/80"></div>
        </div>
        <div className="relative container mx-auto px-4 text-center text-primary-foreground">
          <h2 className="text-5xl font-bold mb-6">Save Lives Through Blood Donation</h2>
          <p className="text-xl mb-8 max-w-2xl mx-auto">
            Connect with blood donors in your area quickly and safely. Every donation can save up to three lives.
          </p>
          <div className="flex justify-center space-x-4">
            <Button variant="emergency" size="lg">
              <Phone className="mr-2 h-5 w-5" />
              Emergency Request
            </Button>
            <Button variant="hero" size="lg">
              <Search className="mr-2 h-5 w-5" />
              Find Donors
            </Button>
          </div>
        </div>
      </section>

      {/* Search Section */}
      <section className="py-16 bg-gradient-subtle">
        <div className="container mx-auto px-4">
          <div className="max-w-4xl mx-auto">
            <h3 className="text-3xl font-bold text-center mb-8">Find Blood Donors Near You</h3>
            
            <Card className="shadow-card">
              <CardHeader>
                <CardTitle className="flex items-center">
                  <Search className="mr-2 h-5 w-5 text-primary" />
                  Quick Search
                </CardTitle>
                <CardDescription>
                  Enter your requirements to find compatible donors in your area
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">Blood Type Needed</label>
                    <Select value={selectedBloodType} onValueChange={setSelectedBloodType}>
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
                    <label className="block text-sm font-medium mb-2">Location</label>
                    <Input 
                      placeholder="Enter city or ZIP code"
                      value={location}
                      onChange={(e) => setLocation(e.target.value)}
                      className="w-full"
                    />
                  </div>
                </div>
                
                <div className="flex justify-center">
                  <Button variant="hero" size="lg" className="w-full md:w-auto">
                    <Search className="mr-2 h-5 w-5" />
                    Search Available Donors
                  </Button>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Statistics Section */}
      <section className="py-16">
        <div className="container mx-auto px-4">
          <h3 className="text-3xl font-bold text-center mb-12">Our Impact</h3>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div className="text-center">
              <div className="w-20 h-20 bg-gradient-primary rounded-full flex items-center justify-center mx-auto mb-4 shadow-medical">
                <Users className="h-10 w-10 text-primary-foreground" />
              </div>
              <h4 className="text-3xl font-bold text-primary mb-2">15,000+</h4>
              <p className="text-muted-foreground">Registered Donors</p>
            </div>
            
            <div className="text-center">
              <div className="w-20 h-20 bg-gradient-secondary rounded-full flex items-center justify-center mx-auto mb-4 shadow-card">
                <Heart className="h-10 w-10 text-secondary-foreground" />
              </div>
              <h4 className="text-3xl font-bold text-primary mb-2">45,000+</h4>
              <p className="text-muted-foreground">Lives Saved</p>
            </div>
            
            <div className="text-center">
              <div className="w-20 h-20 bg-accent rounded-full flex items-center justify-center mx-auto mb-4 shadow-medical">
                <Clock className="h-10 w-10 text-accent-foreground" />
              </div>
              <h4 className="text-3xl font-bold text-primary mb-2">24/7</h4>
              <p className="text-muted-foreground">Emergency Support</p>
            </div>
            
            <div className="text-center">
              <div className="w-20 h-20 bg-warning rounded-full flex items-center justify-center mx-auto mb-4 shadow-emergency">
                <Shield className="h-10 w-10 text-warning-foreground" />
              </div>
              <h4 className="text-3xl font-bold text-primary mb-2">100%</h4>
              <p className="text-muted-foreground">Verified Donors</p>
            </div>
          </div>
        </div>
      </section>

      {/* Blood Type Compatibility */}
      <section className="py-16 bg-gradient-subtle">
        <div className="container mx-auto px-4">
          <h3 className="text-3xl font-bold text-center mb-12">Blood Type Compatibility</h3>
          <div className="max-w-6xl mx-auto">
            <Card className="shadow-card">
              <CardHeader>
                <CardTitle>Universal Donors & Recipients</CardTitle>
                <CardDescription>Understanding blood type compatibility can help save more lives</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                  <div className="text-center">
                    <div className="w-32 h-32 bg-gradient-primary rounded-full flex items-center justify-center mx-auto mb-4 shadow-medical">
                      <span className="text-3xl font-bold text-primary-foreground">O-</span>
                    </div>
                    <h4 className="text-xl font-bold mb-2">Universal Donor</h4>
                    <p className="text-muted-foreground">O- blood type can donate to anyone in emergency situations</p>
                    <Badge className="mt-2 bg-accent text-accent-foreground">High Demand</Badge>
                  </div>
                  
                  <div className="text-center">
                    <div className="w-32 h-32 bg-gradient-secondary rounded-full flex items-center justify-center mx-auto mb-4 shadow-card">
                      <span className="text-2xl font-bold text-secondary-foreground">AB+</span>
                    </div>
                    <h4 className="text-xl font-bold mb-2">Universal Recipient</h4>
                    <p className="text-muted-foreground">AB+ blood type can receive from any donor in emergencies</p>
                    <Badge className="mt-2 bg-secondary text-secondary-foreground">Can Receive All</Badge>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Call to Action */}
      <section className="py-20 bg-gradient-hero">
        <div className="container mx-auto px-4 text-center text-primary-foreground">
          <h3 className="text-4xl font-bold mb-6">Ready to Save Lives?</h3>
          <p className="text-xl mb-8 max-w-2xl mx-auto">
            Join thousands of heroes making a difference in their communities through blood donation.
          </p>
          <div className="flex justify-center space-x-4">
            <Button variant="emergency" size="lg">
              <Heart className="mr-2 h-5 w-5" />
              Register as Donor
            </Button>
            <Button variant="hero" size="lg">
              <MapPin className="mr-2 h-5 w-5" />
              Find Donation Centers
            </Button>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-card border-t border-border py-12">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <div className="flex items-center space-x-2 mb-4">
                <Heart className="h-6 w-6 text-primary" />
                <h4 className="text-lg font-bold">BloodConnect</h4>
              </div>
              <p className="text-muted-foreground">
                Connecting donors with those in need, saving lives one donation at a time.
              </p>
            </div>
            
            <div>
              <h5 className="font-semibold mb-4">Quick Links</h5>
              <ul className="space-y-2 text-muted-foreground">
                <li><a href="#" className="hover:text-primary transition-medical">Find Donors</a></li>
                <li><a href="#" className="hover:text-primary transition-medical">Become Donor</a></li>
                <li><a href="#" className="hover:text-primary transition-medical">Emergency Request</a></li>
                <li><a href="#" className="hover:text-primary transition-medical">Donation Centers</a></li>
              </ul>
            </div>
            
            <div>
              <h5 className="font-semibold mb-4">Support</h5>
              <ul className="space-y-2 text-muted-foreground">
                <li><a href="#" className="hover:text-primary transition-medical">Help Center</a></li>
                <li><a href="#" className="hover:text-primary transition-medical">Contact Us</a></li>
                <li><a href="#" className="hover:text-primary transition-medical">FAQs</a></li>
                <li><a href="#" className="hover:text-primary transition-medical">Safety Guidelines</a></li>
              </ul>
            </div>
            
            <div>
              <h5 className="font-semibold mb-4">Emergency Hotline</h5>
              <div className="space-y-2">
                <p className="text-2xl font-bold text-primary">1-800-BLOOD</p>
                <p className="text-muted-foreground">Available 24/7 for urgent requests</p>
              </div>
            </div>
          </div>
          
          <div className="border-t border-border mt-8 pt-8 text-center text-muted-foreground">
            <p>&copy; 2024 BloodConnect. All rights reserved. Saving lives together.</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Index;