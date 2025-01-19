import React, { useState, useEffect } from 'react';
import { Alert, Button, Form, Modal } from 'react-bootstrap';
import axios from 'axios';
import NavbarComponent from 'Frontend/components/NavbarComponent';

function CreateSaleDetails() {
  const [formData, setFormData] = useState({
    email: '',
    phone: '',
    customerName: '',
    address: '',
    state: '',
    itemId: 0,
    dateOfBirth: '',
    quantity: '',
    price: '',
    payAmount: ''
  });

  const [error, setError] = useState<string | null>(null);
  const [payingValue, setPayingValue] = useState<number | null>(null);
  const [success, setSuccess] = useState<boolean>(false);
  const [showModal, setShowModal] = useState(false);
  const [items, setItems] = useState<{ id: number; itemName: string }[]>([]);

  // Fetch items for the dropdown
  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await axios.get('http://localhost:9000/api/v1/items/all');
        setItems(response.data.data);
      } catch (err) {
        setError('Failed to fetch items.');
      }
    };
    fetchItems();
  }, []);

  // Handle form input changes
  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Submit the form data
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:9000/api/v1/sales-details', formData);
      const responseData = response.data.data;

      if (responseData) {
        setSuccess(true);
        setError(null);
        setPayingValue(responseData.payAmount)
        setShowModal(true); // Show success modal
        setFormData({
          email: '',
          phone: '',
          customerName: '',
          address: '',
          state: '',
          itemId: 0,
          dateOfBirth: '',
          quantity: '',
          price: '',
          payAmount: ''
        })
      }
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || 'Failed to create sale entry.';
      setError(errorMessage);
      setSuccess(false);
    }
  };

  return (
    <div>
      <NavbarComponent />
      <div className="container mt-4">
        <h3>Create Sale Entry</h3>
        {error && <Alert variant="danger">{error}</Alert>}

        <Form onSubmit={handleSubmit} className="card shadow-lg p-4 rounded rounded-3">
          <div className="row">
            {/* Phone */}
            <Form.Group controlId="phone" className="col-md-6">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control
                type="number"
                name="phone" maxLength={10} minLength={10}
                value={formData.phone}
                onChange={handleInputChange}
                required
              />
            </Form.Group>
            
            {/* Email */}
            <Form.Group controlId="email" className="col-md-6">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            {/* Customer Name */}
            <Form.Group controlId="customerName" className="col-md-6 mt-3">
              <Form.Label>Customer Name</Form.Label>
              <Form.Control
                type="text"
                name="customerName"
                value={formData.customerName}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            {/* Address */}
            <Form.Group controlId="address" className="col-md-6 mt-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                type="text"
                name="address"
                value={formData.address}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            {/* State */}
            <Form.Group controlId="state" className="col-md-6 mt-3">
              <Form.Label>State</Form.Label>
              <Form.Control
                type="text"
                name="state"
                value={formData.state}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            {/* Item Dropdown */}
            <Form.Group controlId="itemId" className="col-md-6 mt-3">
              <Form.Label>Item</Form.Label>
              <Form.Select
                name="itemId"
                value={formData.itemId}
                onChange={handleInputChange}
                required
              >
                <option value="">Select an Item</option>
                {items.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.itemName || 'Unnamed Item'}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>

            {/* Date of Birth */}
            <Form.Group controlId="dateOfBirth" className="col-md-6 mt-3">
              <Form.Label>Date of Birth</Form.Label>
              <Form.Control
                type="date"
                name="dateOfBirth"
                value={formData.dateOfBirth}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            {/* Quantity */}
            <Form.Group controlId="quantity" className="col-md-6 mt-3">
              <Form.Label>Quantity</Form.Label>
              <Form.Control
                type="number"
                name="quantity"
                value={formData.quantity}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            {/* Price */}
            <Form.Group controlId="price" className="col-md-6 mt-3">
              <Form.Label>Price</Form.Label>
              <Form.Control
                type="number"
                name="price"
                value={formData.price}
                onChange={handleInputChange}
                required
              />
            </Form.Group>
          </div>

          <Button variant="success" style={{ width: 200 }} type="submit" className="mt-3">
            Save Sale
          </Button>
        </Form>

        {/* Success Modal */}
        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Sale Entry Created Successfully</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {/* Success message here */}
            {success ? (
              <div>
                <p>{`The sale entry for the item has been created successfully.`}</p>
                {payingValue && <p>The sale entry added successfully with a pay amount of {payingValue}.</p>}
              </div>
            ) : (
              <p>Something went wrong while creating the sale entry.</p>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
}

export default CreateSaleDetails;