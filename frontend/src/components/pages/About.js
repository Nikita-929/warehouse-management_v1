import React from 'react';
import { Container, Row, Col, Card, ListGroup } from 'react-bootstrap';

const About = () => {
  return (
    <Container>
      <Row>
        <Col md={8} className="mx-auto">
          <h2>About the Warehouse Management System</h2>
          <p>
            A lightweight warehouse management app for inventory, transactions, import, and reporting.
          </p>

          <Card className="mt-4">
            <Card.Header>
              <h5>System Features</h5>
            </Card.Header>
            <Card.Body>
              <ListGroup variant="flush">
                <ListGroup.Item>Product management (CRUD)</ListGroup.Item>
                <ListGroup.Item>Transaction IN/OUT tracking</ListGroup.Item>
                <ListGroup.Item>Inventory &amp; batch number tracking</ListGroup.Item>
                <ListGroup.Item>Excel upload (bulk import) — supports Indian date format</ListGroup.Item>
                <ListGroup.Item>CSV / Excel export &amp; reports</ListGroup.Item>
                <ListGroup.Item>Real-time autocomplete suggestions</ListGroup.Item>
                <ListGroup.Item>Advanced search and filtering</ListGroup.Item>
                <ListGroup.Item>Responsive modern UI</ListGroup.Item>
              </ListGroup>
            </Card.Body>
          </Card>

          
          
        </Col>
      </Row>
    </Container>
  );
};

export default About;
