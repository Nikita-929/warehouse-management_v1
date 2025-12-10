import React from "react";
import { Container, Row, Col } from "react-bootstrap";

const Footer = () => {
  return (
    <footer className="footer mt-5 py-4">
      <Container>
        <Row>
          <Col md={6}>
            <h5>X Technologies</h5>
            <p className="mb-1">
              <i className="bi bi-geo-alt"></i> 123 Warehouse Street, Industrial
              Area, City 100001
            </p>
            <p className="mb-1">
              <i className="bi bi-envelope"></i>{" "}
              <a href="mailto:contact@xtech.com" className="text-white">
                contact@xtech.com
              </a>
            </p>
            <p className="mb-1">
              <i className="bi bi-telephone"></i> +1 (555) 123-4567
            </p>
          </Col>
          <Col md={3}>
            <h5>Quick Links</h5>
            <ul className="list-unstyled">
              <li>
                <a href="/" className="text-white">
                  Dashboard
                </a>
              </li>
              <li>
                <a href="/products/add" className="text-white">
                  Add Product
                </a>
              </li>
              <li>
                <a href="/products/list" className="text-white">
                  Product List
                </a>
              </li>
              <li>
                <a href="/reports" className="text-white">
                  Reports
                </a>
              </li>
              <li>
                <a href="/upload" className="text-white">
                  Upload
                </a>
              </li>
              <li>
                <a href="/about" className="text-white">
                  About
                </a>
              </li>
            </ul>
          </Col>
        </Row>
        <hr className="my-4 bg-light" />
        <Row>
          <Col md={6}>
            <p className="mb-0">&copy; X Technologies. All rights reserved.</p>
          </Col>
          <Col md={6} className="text-md-end">
            <p className="mb-0">Warehouse Management System v2.1</p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
