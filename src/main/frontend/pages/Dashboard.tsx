import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, Pagination, Alert, Button, Card, Row, Col } from 'react-bootstrap';
import NavbarComponent from 'Frontend/components/NavbarComponent';

interface SalesData {
  itemName: string;
  customerName: string;
  monthEndDate: number;
  totalPaymentLastMonth: number;
  currentMonthPayment: number;
  totalPaymentCurrentMonth: number;
}

interface CustomerData {
  name: string;
  value: number;
}

interface DashboardProps {}

const Dashboard: React.FC<DashboardProps> = () => {
  const [salesData, setSalesData] = useState<SalesData[]>([]);
  const [topCustomersPayment, setTopCustomersPayment] = useState<CustomerData[]>([]);
  const [topCustomersShopping, setTopCustomersShopping] = useState<CustomerData[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    // Fetch data for itemwise and customerwise payments
    axios
      .get('http://localhost:9000/api/v1/dashboard/itemwise-customerwise-payments')
      .then((response) => {
        setSalesData(response.data);
      })
      .catch((error) => {
        setError('Failed to fetch data');
        console.error(error);
      })
      .finally(() => setLoading(false));

    // Fetch top 5 customers by payment collected
    axios
      .get('http://localhost:9000/api/v1/dashboard/top-customers-payment')
      .then((response) => {
        setTopCustomersPayment(response.data);
      })
      .catch((error) => {
        setError('Failed to fetch top customers by payment');
        console.error(error);
      });

    // Fetch top 10 customers by shopping frequency
    axios
      .get('http://localhost:9000/api/v1/dashboard/top-customers-shopping')
      .then((response) => {
        setTopCustomersShopping(response.data);
      })
      .catch((error) => {
        setError('Failed to fetch top customers by shopping frequency');
        console.error(error);
      });
  }, []);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // Fetch top 5 customers by payment
        const paymentResponse = await axios.get('http://localhost:9000/api/v1/dashboard/top-customers-payment');
        const paymentData: CustomerData[] = paymentResponse.data.map((customer: any) => ({
          name: customer[0],
          value: customer[1],
        }));
        setTopCustomersPayment(paymentData);
  
        // Fetch top 10 customers by shopping frequency
        const shoppingResponse = await axios.get('http://localhost:9000/api/v1/dashboard/top-customers-shopping');
        const shoppingData: CustomerData[] = shoppingResponse.data.map((customer: any) => ({
          name: customer[0],
          value: customer[1],
        }));
        setTopCustomersShopping(shoppingData);
      } catch (error) {
        setError('Failed to fetch dashboard data.');
        console.error(error);
      }
    };
  
    fetchDashboardData();
  }, []);

  return (
    <div>
      <NavbarComponent/>
      <div className="container mt-5">
        {error && <Alert variant="danger">{error}</Alert>}
        <Row>
          {/* Itemwise & Customerwise Payments Section */}
          <Col md={6} className="mb-4">
            <Card>
              <Card.Header>Itemwise and Customerwise Payment Data</Card.Header>
              <Card.Body>
                {loading ? (
                  <div>Loading...</div>
                ) : (
                  <Table striped bordered hover responsive>
                    <thead>
                      <tr>
                        <th>Item Name</th>
                        <th>Customer Name</th>
                        <th>Month End Date</th>
                        <th>Total Payment (Last Month)</th>
                        <th>Current Month Payment</th>
                        <th>Total Payment (Current Month)</th>
                      </tr>
                    </thead>
                    <tbody>
                      {salesData.length > 0 ? (
                        salesData.map((sale, index) => (
                          <tr key={index}>
                            <td>{sale.itemName}</td>
                            <td>{sale.customerName}</td>
                            <td>{sale.monthEndDate}</td>
                            <td>{sale.totalPaymentLastMonth}</td>
                            <td>{sale.currentMonthPayment}</td>
                            <td>{sale.totalPaymentCurrentMonth}</td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan={6} className="text-center">
                            No data found
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </Table>
                )}
              </Card.Body>
            </Card>
          </Col>

          {/* Top 5 Customers by Payment Section */}
          <Col md={6} className="mb-4">
            <Card>
              <Card.Header>Top 5 Customers by Payment Collected</Card.Header>
              <Card.Body>
                {loading ? (
                  <div>Loading...</div>
                ) : (
                  <Table striped bordered hover responsive>
                    <thead>
                      <tr>
                        <th>Customer Name</th>
                        <th>Total Payment Collected</th>
                      </tr>
                    </thead>
                    <tbody>
                      {topCustomersPayment.length > 0 ? (
                        topCustomersPayment.map((customer, index) => (
                          <tr key={index}>
                            <td>{customer.name}</td>
                            <td>{customer.value}</td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan={2} className="text-center">
                            No data found
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </Table>
                )}
              </Card.Body>
            </Card>
          </Col>
        </Row>

        {/* Top 10 Customers by Shopping Frequency */}
        <Row>
          <Col md={12} className="mb-4">
            <Card>
              <Card.Header>Top 10 Customers by Shopping Frequency</Card.Header>
              <Card.Body>
                {loading ? (
                  <div>Loading...</div>
                ) : (
                  <Table striped bordered hover responsive>
                    <thead>
                      <tr>
                        <th>Customer Name</th>
                        <th>Number of Purchases</th>
                      </tr>
                    </thead>
                    <tbody>
                      {topCustomersShopping.length > 0 ? (
                        topCustomersShopping.map((customer, index) => (
                          <tr key={index}>
                            <td>{customer.name}</td>
                            <td>{customer.value}</td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan={2} className="text-center">
                            No data found
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </Table>
                )}
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default Dashboard;