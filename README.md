# Shopping Bucket Application

This project is a simple web application designed to manage items and sales details, with functionality to perform CRUD operations, validations, and reporting. The application uses a database for persistent storage, supports pagination for large datasets, and handles concurrency effectively.

## Features

### Item Management
1. **Item Master (ITEMMAS) Table**:
   - **Fields**: Item ID, Item Name, Item Quantity.
   - Functionality:
     - Create new items.
     - Update item details.
     - Ensure items are available for sale before selection.

### Sales Management
1. **Sales Details Table (SALEDETAILS)**:
   - **Fields**: Shop Date, Mobile Number, Customer Name, Item ID, Address, State, Date of Birth, Minor, Quantity, Price, Pay Amount, Email ID.
   - Functionality:
     - Add new sales entries with validations.
     - Update existing sales entries.
     - Cancel sales entries.

2. **Validations**:
   - Proper date format for all dates.
   - Valid mobile numbers.
   - Valid email IDs.
   - Ensure selected items exist in the item master and are available for sale.
   - If the customer is a minor (under 18), restrict purchases to ₹1000.
   - Apply a 20% discount for customers from Maharashtra.

### Search and Display
1. **Search Functionality**:
   - Search sales details by:
     - Item Name.
     - Customer Name.
     - Mobile Number.
     - Payment Amount.
   - Apply pagination if the search results exceed 20 records.

2. **Reports**:
   - Display item-wise and customer-wise total payment amounts:
     - Up to the last month.
     - For the current month.
     - Total payment collected up to the current month.
   - Top 5 customers based on payment collected.
   - Top 10 customers based on shopping frequency.

### Reporting
1. **Dashboard**:
   - Detailed insights on sales performance:
     - Item-wise and customer-wise payment statistics.
     - Monthly breakdown of payments.
     - Top customers by payment and shopping frequency.

---

## Technologies Used

1. **Frontend**: [React ts.]
2. **Backend/API**: [Spring Boot ]
3. **Database**: [h2 Relational data]

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/bhupendrasambare/shopping-bucket.git
   ```
2. Set up the database schema and tables using the provided scripts.
3. Update the backend configuration with your database credentials.
4. Start the backend server.
5. Launch the frontend application.
6. Access the application at `http://localhost:9000`.

---

## Validation Checklist

1. **Basic Validations**:
    - Proper date format: ✅
    - Valid mobile numbers: ✅
    - Valid email IDs: ✅
    - Ensure selected items are valid and available for sale: ✅

2. **Key Features Covered**:
    - **Item Management**: ✅
    - **Sales Entry and Update**: ✅
    - **Cancel Sales**: ✅
    - **Search and Display**: ✅
    - **Reports and Insights**:
        - Item-wise and customer-wise payments: ✅
        - Top customers by payment and shopping frequency: ✅

---

## Assignment Evaluation

### To be filled by the candidate:

1. **Frontend**: [Specify your choice, e.g., React]
2. **Backend/API**: [Specify your choice, e.g., Spring Boot]
3. **Number of Validations Implemented**: [1-5]
4. **Points Covered**: [1-8]

---

## Acknowledgments

This project is created as part of an assignment to demonstrate full-stack application development skills, including frontend, backend, and database integration.