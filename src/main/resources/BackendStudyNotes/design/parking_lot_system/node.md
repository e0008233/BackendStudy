Design a parking lot. 
Handle ambiguity and ask question to clarify. 
- How is the parking lot designed? Building, open space
- Accessibility, size of parking lot
- Multi levels: dependency: top level to be filled 
- Size of the car

Abstract Class Vehicle 
- String licensePlate;
- Enum color

Class MotorCycle(S), Car(M), Truck(L), Bus(XL) extend the Class Vehicle

Class ParkingLot
- Int ZipCode
+ Spot: PlaceVehicle(Vehicle Vehicle)

Class Spot
- Long id
- Enum size

Data structure
PlaceVehicle(Vehicle Vehicle)
•	4 stacks to hold all available spots for different vehicle sizes 
•	O(1) for time complexity to check if 4 stacks are empty or not
•	Put the vehicle in the Hashmap for easier removal
RemoveVehicle(Vehicle Vehicle)
•	O(1) for time complexity for the hashmap lookup

