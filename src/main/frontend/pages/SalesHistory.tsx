import React, { useState, useEffect } from 'react';
import { Button, Modal, Table, Pagination, Alert, Form } from 'react-bootstrap';
import axios from 'axios';
import NavbarComponent from 'Frontend/components/NavbarComponent'; // Ensure this is correct

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
  price: number|null;
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

function SalesHistory() {
  const [salesDetails, setSalesDetails] = useState<SalesData[]>([]);
  const [items, setItems] = useState<{ id: number; itemName: string }[]>([]);
  const [formData, setFormData] = useState<{
    itemName: string;
    customerName: string;
    itemId: number | null; // Ensure itemId can be null or a number
    phone: string;
    price: number | null |any;
    email: string;
    quantity: number;
    startAmount: number;
    endAmount: number;
    page: number;
    size: number;
  }>({
    itemName: '',
    customerName: '',
    itemId: null, // Initialize itemId as null
    phone: '',
    price: 0,
    email: '',
    quantity: 0,
    startAmount: 0,
    endAmount: 5000,
    page: 0,
    size: 10,
  });
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);
  const [showViewModal, setShowViewModal] = useState(false);
  const [selectedSale, setSelectedSale] = useState<SalesData | null>(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [saleToDelete, setSaleToDelete] = useState<number | null>(null);
  const [showEditModal, setShowEditModal] = useState(false);
  // Fetch available items for the dropdown

  // Fetch sales data from the API
  const fetchSales = async () => {
    try {
      const response = await axios.post<SalesResponse>(
        'http://localhost:9000/api/v1/sales-details/search',
        formData
      );
      const data = response.data.data;
      setSalesDetails(data.data);
      setTotalPages(Math.ceil(data.total / data.size));
    } catch (err) {
      setError('Failed to fetch sales details.');
      console.error(err);
    }
  };

  const handleDelete = async () => {
    if (saleToDelete !== null) {
      try {
        const response = await axios.delete(`http://localhost:9000/api/v1/sales-details/${saleToDelete}`);
        setSuccessMessage(response.data.message || 'Sale deleted successfully');
        fetchSales(); // Refresh sales data
      } catch (err: any) {
        const errorMessage = err.response?.data?.message || 'Failed to delete the sale.';
        setError(errorMessage);
      } finally {
        setShowDeleteModal(false);
        setSaleToDelete(null);
      }
    }
  };

  const handleEditSale = async (saleId: number | undefined) => {
    try {
      if (!saleId) return;
      const updatedSale = {
        ...formData, // Use the form data for the update request
      };
      const response = await axios.put(
        `http://localhost:9000/api/v1/sales-details/${saleId}`,
        updatedSale
      );
      setSuccessMessage('Sale updated successfully');
      fetchSales(); // Refresh sales data
      setShowEditModal(false);
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.message || 'Failed to update the sale.';
      setError(errorMessage);
    }
    setFormData({
      itemName: '', 
      customerName: '',
      itemId:null,
      phone: "",
      price: 0,
      email: "", 
      quantity: 0,
      startAmount: 0, 
      endAmount: 10000, 
      page: 0,
      size: 10,
    });
  };

  useEffect(() => {
    fetchSales();
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

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await axios.get('http://localhost:9000/api/v1/items/all');
        setItems(response.data.data);
      } catch (err) {
        setError('Failed to fetch items.');
        console.error(err);
      }
    };
    fetchItems();
  }, []);

  const handleView = (sale: SalesData) => {
    setSelectedSale(sale);
    setShowViewModal(true);
  };

  return (
    <div className="vh-100 d-flex flex-column">
      <NavbarComponent />
      <div className="container mt-4">
        {error && <Alert variant="danger">{error}</Alert>}
        {successMessage && <Alert variant="success">{successMessage}</Alert>}

        <div className="card shadow-sm mb-4">
          <div className="card-body">
            <h5 className="card-title">Search Sales</h5>
            <Form>
              <Form className="row">

                <Form.Group controlId="itemName" className="col-md-3">
                  <Form.Label>Item Name</Form.Label>
                  <Form.Control
                    placeholder='Potato'
                    type="text"
                    name="itemName"
                    value={formData.itemName}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                <Form.Group controlId="customerName" className="col-md-3">
                  <Form.Label>Customer Name</Form.Label>
                  <Form.Control
                    placeholder='Bhupendra sambare'
                    type="text"
                    name="customerName"
                    value={formData.customerName}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                <Form.Group controlId="phone" className="col-md-3">
                  <Form.Label>Phone</Form.Label>
                  <Form.Control
                    placeholder='9516138020'
                    type="text"
                    name="phone"
                    value={formData.phone}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </Form>

              <Form className="row">
                <Form.Group controlId="startAmount" className="col-md-3">
                  <Form.Label>Minimum Price</Form.Label>
                  <Form.Control
                    placeholder='0'
                    type="number"
                    name="startAmount"
                    value={formData.startAmount}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                <Form.Group controlId="endAmount" className="col-md-3">
                  <Form.Label>Maximum Price</Form.Label>
                  <Form.Control
                    placeholder='9999'
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
              <Button variant="primary" className='mt-3' onClick={fetchSales}>
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
                <th>Minor</th>
                <th>Item</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Paid</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {salesDetails.length > 0 ? (
                salesDetails.map((sale) => (
                  <tr key={sale.id}>
                    <td>{sale.id}</td>
                    <td>{sale.items?.itemName || 'Unnamed Item'}</td>
                    <td>{sale.customerName}</td>
                    <td>{sale.phone}</td>
                    <td>{sale.minor && sale.minor?
                    (<button className='btn btn-sm btn-warning rounded-pill'>Minor</button>)
                    :(<button className='btn btn-sm btn-primary rounded-pill'>Adult</button>)}</td>
                    <td>{sale?.items?.itemName}</td>
                    <td>{sale.quantity}</td>
                    <td>{sale.price}</td>
                    <td>{sale.payAmount}</td>
                    <td>
                      <Button variant="info" size="sm" onClick={() => handleView(sale)}>
                        View
                      </Button>{' '}
                      <Button
                        variant="warning"
                        size="sm"
                        className="m-2"
                        onClick={() => {
                          setSelectedSale(sale);  // Set selected sale to be edited
                          setFormData({
                            itemName: sale.items?.itemName || '', 
                            customerName: sale.customerName,
                            itemId:sale.items.id,
                            phone: sale.phone,
                            price: sale.price,
                            email: sale.email, 
                            quantity:sale.quantity,
                            startAmount: sale.price || 0, 
                            endAmount: sale.payAmount || 0, 
                            page: 0,
                            size: 10,
                          });
                          setShowEditModal(true); // Open the modal for editing
                        }}
                      >
                        Edit
                      </Button>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => {
                          setSaleToDelete(sale.id);
                          setShowDeleteModal(true);
                        }}
                      >
                        Delete
                      </Button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={8} className="text-center">
                    No data found
                  </td>
                </tr>
              )}
            </tbody>
          </Table>


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

        {/* View Modal */}
        <Modal show={showViewModal} onHide={() => setShowViewModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Sale Details</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {selectedSale && (
              <div>
                <p><strong>ID:</strong> {selectedSale.id}</p>
                <p><strong>Customer Name:</strong> {selectedSale.customerName}</p>
                <p><strong>Email:</strong> {selectedSale.email}</p>
                <p><strong>Phone:</strong> {selectedSale.phone}</p>
                <p><strong>Minor:</strong> {selectedSale.minor && selectedSale.minor?
                  (<span className='btn-sm btn btn-warning'>Minor</span>) :(<span className='btn-sm btn btn-primary'>Adult</span>)}
                </p>
                <p><strong>Address:</strong> {selectedSale.address}</p>
                <p><strong>State:</strong> {selectedSale.state}</p>
                <p><strong>Item Name:</strong> {selectedSale.items?.itemName || 'Unnamed Item'}</p>
                <p><strong>Total amount:</strong> {selectedSale.price || 'N/A'}</p>
                <p><strong>Amount Paid:</strong> {selectedSale.payAmount || 'N/A'}</p>
                <p><strong>Date of Birth:</strong> {selectedSale.dateOfBirth}</p>
                <p><strong>Quantity:</strong> {selectedSale.quantity}</p>
                <p><strong>Price:</strong> {selectedSale.price}</p>
              </div>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowViewModal(false)}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
        {/* Delete Confirmation Modal */}
        <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Deletion</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Are you sure you want to delete the sale with ID <strong>{saleToDelete}</strong>?
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
              Cancel
            </Button>
            <Button variant="danger" onClick={handleDelete}>
              Delete
            </Button>
          </Modal.Footer>
        </Modal>
        {/* Edit Sale Modal */}
        <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Edit Sale</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {selectedSale && (
              <Form>
                <Form.Group controlId="itemId">
                  <Form.Label>Item</Form.Label>
                  <Form.Control
                    as="select"
                    name="itemId"
                    value={formData.itemId || ''}
                    onChange={handleInputChange}
                  >
                    <option value="">Select Item</option>
                    {items.map((item) => (
                      <option key={item.id} value={item.id}>
                        {item.itemName}
                      </option>
                    ))}
                  </Form.Control>
                </Form.Group>

                <Form.Group controlId="customerName" className="mt-3">
                  <Form.Label>Customer Name</Form.Label>
                  <Form.Control
                    type="text"
                    name="customerName"
                    value={formData.customerName}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                <Form.Group controlId="phone" className="mt-3">
                  <Form.Label>Phone</Form.Label>
                  <Form.Control
                    type="text"
                    name="phone"
                    value={formData.phone}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                {/* Allow Email Update */}
                <Form.Group controlId="email" className="mt-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email || ''} // Initialize email as empty string if not available
                    onChange={handleInputChange}
                  />
                </Form.Group>

                {/* Allow Quantity Update */}
                <Form.Group controlId="quantity" className="mt-3">
                  <Form.Label>Quantity</Form.Label>
                  <Form.Control
                    type="number"
                    name="quantity"
                    value={formData.quantity || 0} // Initialize quantity if not available
                    onChange={handleInputChange}
                  />
                </Form.Group>

                {/* Allow Price Update */}
                <Form.Group controlId="price" className="mt-3">
                  <Form.Label>Price</Form.Label>
                  <Form.Control
                    type="number"
                    name="price"
                    value={formData.price}
                    onChange={handleInputChange}
                  />
                </Form.Group>

                {/* Add more fields as needed */}
              </Form>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowEditModal(false)}>
              Close
            </Button>
            <Button
              variant="primary"
              onClick={() => handleEditSale(selectedSale?.id)}
            >
              Save Changes
            </Button>
          </Modal.Footer>
        </Modal>


      </div>
    </div>
  );
}

export default SalesHistory;
