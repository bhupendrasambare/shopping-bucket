import React, { useState, useEffect } from 'react';
import { Button, Modal, Table, Form, Pagination, Alert } from 'react-bootstrap';
import axios from 'axios';
import { Item } from 'Frontend/interface/data';
import NavbarComponent from 'Frontend/components/NavbarComponent';

function Items() {
  const [items, setItems] = useState<Item[]>([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedItem, setSelectedItem] = useState<Item | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [isUpdate, setIsUpdate] = useState(false);
  const [formData, setFormData] = useState({ itemName: '', itemQty: '' });
  const [error, setError] = useState<string | null>(null);

  const fetchItems = async () => {
    try {
      const response = await axios.get<{ data: { data: Item[]; total: number; size: number } }>(
        `/api/v1/items?page=${page}&size=${size}`
      );
      const responseData = response.data.data;
      setItems(responseData.data);
      setTotalPages(Math.ceil(responseData.total / responseData.size));
    } catch (err) {
      console.error('Error fetching items:', err);
      setError('Failed to fetch items.');
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async () => {
    try {
      if (isUpdate && selectedItem) {
        await axios.put(`/api/v1/items/${selectedItem.id}`, formData);
      } else {
        await axios.post('/api/v1/items', formData);
      }
      fetchItems();
      setShowModal(false);
      setFormData({ itemName: '', itemQty: '' });
    } catch (err) {
      console.error('Error saving item:', err);
      setError('Failed to save the item.');
    }
  };

  const handleDelete = async () => {
    try {
      if (selectedItem) {
        const response = await axios.delete(`/api/v1/items/${selectedItem.id}`);
        // If the response contains a custom error structure, display the message
        if (response.data && response.data.code === "409" && response.data.status === "FAIL") {
          setError(response.data.message); // Set the error message from the API response
        } else {
          fetchItems(); // If no error, refresh the list of items
          setShowDeleteModal(false);
        }
      }
    } catch (err: any) {
      console.error('Error deleting item:', err);
      // Check if the error response has the expected structure and show the message
      if (err.response && err.response.data) {
        const { message } = err.response.data;
        setError(message || 'Failed to delete the item.');
      } else {
        setError('Failed to delete the item.');
      }
    }
  };

  const handlePageChange = (pageNumber: number) => {
    setPage(pageNumber - 1);
  };

  const openModal = (item: Item | null = null) => {
    setIsUpdate(!!item);
    setSelectedItem(item);
    setFormData({
        itemName: item?.itemName || '',
        itemQty: item?.itemQty ? item.itemQty.toString() : '',
      });
    setShowModal(true);
  };

  useEffect(() => {
    fetchItems();
  }, [page]);

  useEffect(() => {
    if (error) {
      // Set a timer to hide the alert after 5 seconds
      setTimeout(() => {
        setError(null);
      }, 5000);
    }
  }, [error]); // Only run when error state changes

  return (
    <div className="vh-100 d-flex flex-column">
      {/* Navbar */}
      <NavbarComponent />

      {/* Main content */}
      <div className="container mt-4">
        {error && <Alert variant="danger">{error}</Alert>}

        <div className="card shadow-sm mb-4">
          <div className="card-body">
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h5 className="card-title">Items List</h5>
              <Button variant="success" onClick={() => openModal()} className="btn-sm">
                Add Item
              </Button>
            </div>
    
            <Table striped bordered hover responsive>
              <thead>
                <tr className="fw-bold">
                  <th>ID</th>
                  <th>Item Name</th>
                  <th>Quantity</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {items.map((item) => (
                  <tr key={item.id}>
                    <td>{item.id}</td>
                    <td>{item.itemName}</td>
                    <td>{item.itemQty}</td>
                    <td>
                      <div className="d-flex">
                        <Button
                          variant="warning"
                          size="sm"
                          onClick={() => openModal(item)}
                          className="me-2"
                        >
                          Update
                        </Button>
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => {
                            setSelectedItem(item);
                            setShowDeleteModal(true);
                          }}
                        >
                          Delete
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>

            {/* Pagination */}
            <Pagination className="justify-content-center">
              {[...Array(totalPages).keys()].map((number) => (
                <Pagination.Item
                  key={number}
                  active={number === page}
                  onClick={() => handlePageChange(number + 1)}
                >
                  {number + 1}
                </Pagination.Item>
              ))}
            </Pagination>
          </div>
        </div>


        <Modal show={showModal} onHide={() => setShowModal(false)}>
            <Modal.Header closeButton>
            <Modal.Title>{isUpdate ? 'Update Item' : 'Create Item'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
            <Form>
                <Form.Group controlId="itemName">
                <Form.Label>Item Name</Form.Label>
                <Form.Control
                    type="text"
                    name="itemName"
                    value={formData.itemName}
                    onChange={handleInputChange}
                />
                </Form.Group>
                <Form.Group controlId="itemQty" className="mt-3">
                <Form.Label>Item Quantity</Form.Label>
                <Form.Control
                    type="number"
                    name="itemQty"
                    value={formData.itemQty}
                    onChange={handleInputChange}
                />
                </Form.Group>
            </Form>
            </Modal.Body>
            <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
                Close
            </Button>
            <Button variant="primary" onClick={handleSubmit}>
                Save
            </Button>
            </Modal.Footer>
        </Modal>
        <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
            <Modal.Header closeButton>
            <Modal.Title>Confirm Deletion</Modal.Title>
            </Modal.Header>
            <Modal.Body>
            Are you sure you want to delete the item <strong>{selectedItem?.itemName}</strong>?
            </Modal.Body>
            <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                Cancel
            </Button>
            <Button
                variant="danger"
                onClick={() => {
                handleDelete();
                setShowDeleteModal(false);
                }}
            >
                Delete
            </Button>
            </Modal.Footer>
        </Modal>
        </div>

    </div>
  );
}

export default Items;
