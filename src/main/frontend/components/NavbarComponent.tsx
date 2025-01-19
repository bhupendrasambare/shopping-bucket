import React from 'react'
import { Container, Nav, Navbar } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom';

function NavbarComponent() {
  const navigate = useNavigate();
  const isActive = (pathname: string) => location.pathname === pathname;
  return (
    <div>
        <Navbar expand="md" variant="dark" bg="dark" className="custom-navbar" aria-label="Stylez navigation bar">
            <Container>
                <Navbar.Brand className="cursor-pointer" onClick={()=>navigate("/")}> Sales<span>.</span>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarsStylez" />
                <Navbar.Collapse id="navbarsStylez">
                <Nav className="ms-auto mb-2 mb-md-0">
                    <Nav.Link onClick={()=>navigate("/items")} className={isActive("/items") ? "active" : ""} >Items</Nav.Link>
                    <Nav.Link onClick={()=>navigate("/sales")} className={isActive("/sales") ? "active" : ""} >Sales History</Nav.Link>
                </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    </div>
  )
}

export default NavbarComponent