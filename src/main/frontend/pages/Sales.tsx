import React, { useState, useEffect } from 'react';
import { Button, Modal, Table, Pagination, Alert, Form } from 'react-bootstrap';
import axios from 'axios';
import NavbarComponent from 'Frontend/components/NavbarComponent'; // Ensure this is correct

// Define interfaces for the sales data types
interface SalesData {
  id: number;
  email: string;
  phone: string;
  customerName: string;
  address: string;
  state: string;
  minor: boolean;
  shopDate: string;
  items: {
    id: number;
    itemName: string;
    itemQty: number;
  };
  dateOfBirth: string;
  quantity: number;
  price: number;
  payAmount: number | null;
}

interface SalesResponse {
  code: string;
  status: string;
  message: string;
  data: {
    data: SalesData[];
    page: number;
    total: number;
    size: number;
  };
}

function Sales() {
  const [salesDetails, setSalesDetails] = useState<SalesData[]>([]);
  const [items, setItems] = useState<{ id: number; itemName: string }[]>([]); // Store available items
  const [formData, setFormData] = useState({
    itemId: null,  // Initialize itemId as null
    customerName: '',
    phone: '',
    startAmount: 0,
    endAmount: 5000,
    page: 0,
    size: 10,
  });
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);

  // Fetch available items for the dropdown
  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await axios.get('http://localhost:9000/api/v1/items/all');
        setItems(response.data.data); // Save the items for dropdown
      } catch (err) {
        setError('Failed to fetch items.');
        console.error(err);
      }
    };
    fetchItems();
  }, []);

  // Fetch sales data from the API
  const fetchSales = async () => {
    try {
      // Send only itemId, customerName, phone, startAmount, and endAmount
      const response = await axios.post<SalesResponse>(
        'http://localhost:9000/api/v1/sales-details/search',
        formData
      );
      const data = response.data.data;
      setSalesDetails(data.data);
      setTotalPages(Math.ceil(data.total / data.size));  // Calculate total pages for pagination
    } catch (err) {
      setError('Failed to fetch sales details.');
      console.error(err);
    }
  };

  useEffect(() => {
    fetchSales(); // Fetch sales whenever formData page or size is updated
  }, [formData.page, formData.size]);

  const handlePageChange = (pageNumber: number) => {
    setFormData({ ...formData, page: pageNumber });
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <div className="vh-100 d-flex flex-column">
      <NavbarComponent />
      <div className="container mt-4">
        {error && <Alert variant="danger">{error}</Alert>}

        <div className="card shadow-sm mb-4">
          <div className="card-body">
            <h5 className="card-title">Search Sales</h5>
            <Form>
              <Form className='row'>
                <Form.Group controlId="itemId" className="col-md-3">
                  <Form.Label>Item ID</Form.Label>
                  <Form.Select
                    name="itemId"
                    value={formData.itemId ?? ''} // Ensure null value is handled as an empty string
                    onChange={handleInputChange}
                  >
                    <option value="">Select an Item</option>
                    {items.map((item) => (
                      <option key={item.id} value={item.id}>
                        {item.itemName || 'Unnamed Item'}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <Form.Group controlId="customerName" className="col-md-3">
                  <Form.Label>Customer Name</Form.Label>
                  <Form.Control
                    type="text"
                    name="customerName"
                    value={formData.customerName}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                <Form.Group controlId="phone" className="col-md-3">
                  <Form.Label>Phone</Form.Label>
                  <Form.Control
                    type="text"
                    name="phone"
                    value={formData.phone}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </Form>

              <Form className='row'>
                <Form.Group controlId="startAmount" className="col-md-3">
                  <Form.Label>Start Amount</Form.Label>
                  <Form.Control
                    type="number"
                    name="startAmount"
                    value={formData.startAmount}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                <Form.Group controlId="endAmount" className="col-md-3">
                  <Form.Label>End Amount</Form.Label>
                  <Form.Control
                    type="number"
                    name="endAmount"
                    value={formData.endAmount}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                <Form.Group controlId="size" className="col-md-3">
                  <Form.Label>Results per Page</Form.Label>
                  <Form.Control
                    as="select"
                    name="size"
                    value={formData.size}
                    onChange={handleInputChange}
                  >
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="20">20</option>
                  </Form.Control>
                </Form.Group>
              </Form>
              <Button variant="primary" onClick={fetchSales}>
                Search
              </Button>
            </Form>
          </div>
        </div>

        <div className="card shadow-sm p-4">
          <h5>Sales Results</h5>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Item Name</th>
                <th>Customer Name</th>
                <th>Phone</th>
                <th>Amount</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {salesDetails.map((sale) => (
                <tr key={sale.id}>
                  <td>{sale.id}</td>
                  <td>{sale.items?.itemName || 'Unnamed Item'}</td>
                  <td>{sale.customerName}</td>
                  <td>{sale.phone}</td>
                  <td>{sale.payAmount ? sale.payAmount : 'N/A'}</td>
                  <td>
                    <Button variant="info" size="sm">
                      View
                    </Button>{' '}
                    <Button variant="warning" size="sm">
                      Update
                    </Button>{' '}
                    <Button variant="danger" size="sm">
                      Delete
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>

          {/* Pagination */}
          <Pagination>
            {[...Array(totalPages).keys()].map((number) => (
              <Pagination.Item
                key={number}
                active={number === formData.page}
                onClick={() => handlePageChange(number)}
              >
                {number + 1}
              </Pagination.Item>
            ))}
          </Pagination>
        </div>
      </div>
    </div>
  );
}

export default Sales;
