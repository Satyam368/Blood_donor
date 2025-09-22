export const registerDonor = async (donorData) => {
  const response = await fetch("http://localhost:8080/api/donors", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(donorData),
  });

  if (!response.ok) {
    throw new Error("Failed to register donor");
  }

  return response.json();
};
