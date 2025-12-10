import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Form,
  InputGroup,
} from "react-bootstrap";
import { toast } from "react-toastify";
import { BiSave } from "react-icons/bi";
import AutocompleteInput from "../common/AutocompleteInput";
import { productService } from "../../services/api";

const AddProduct = () => {
  const formatISTDateTimeLocal = (date) => {
    // Convert to IST and return yyyy-MM-ddTHH:mm string for datetime-local
    const istString = date.toLocaleString("en-US", {
      timeZone: "Asia/Kolkata",
    });
    const istDate = new Date(istString);
    const pad = (n) => String(n).padStart(2, "0");
    const yyyy = istDate.getFullYear();
    const MM = pad(istDate.getMonth() + 1);
    const dd = pad(istDate.getDate());
    const HH = pad(istDate.getHours());
    const mm = pad(istDate.getMinutes());
    return `${yyyy}-${MM}-${dd}T${HH}:${mm}`;
  };

  const [formData, setFormData] = useState({
    productCode: "",
    productName: "",
    packets: 0,
    qtyPerPacket: 0,
    quantity: 0,
    unit: "",
    batchNo: "",
    grnNo: "",
    salesInvoiceNo: "",
    materialType: "",
    source: "",
    dateAdded: "",
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Set current date and time in IST
    const now = new Date();
    const formattedDate = formatISTDateTimeLocal(now);
    setFormData((prev) => ({
      ...prev,
      dateAdded: formattedDate,
    }));
  }, []);

  useEffect(() => {
    // Calculate quantity when packets or qtyPerPacket changes
    const packets = parseFloat(formData.packets) || 0;
    const qtyPerPacket = parseFloat(formData.qtyPerPacket) || 0;
    const calculatedQty = packets * qtyPerPacket;

    setFormData((prev) => ({
      ...prev,
      quantity: calculatedQty,
    }));
  }, [formData.packets, formData.qtyPerPacket]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handlePacketsChange = (increment) => {
    const currentPackets = parseFloat(formData.packets) || 0;
    const newPackets = Math.max(0, currentPackets + increment);
    setFormData((prev) => ({
      ...prev,
      packets: newPackets.toFixed(2),
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await productService.addProduct(formData);
      if (response.success) {
        toast.success("Product added successfully!");
        // Reset form
        setFormData({
          productCode: "",
          productName: "",
          packets: 0,
          qtyPerPacket: 0,
          quantity: 0,
          unit: "",
          batchNo: "",
          grnNo: "",
          salesInvoiceNo: "",
          materialType: "",
          source: "",
          dateAdded: formatISTDateTimeLocal(new Date()),
        });
      } else {
        toast.error(response.message || "Error adding product");
      }
    } catch (error) {
      toast.error(error.message || "Error adding product");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container>
      <Card className="shadow-sm mb-4">
        <Card.Header className="bg-white">
          <h2 className="text-center mb-0">Add New Product</h2>
        </Card.Header>
        <Card.Body>
          <Form onSubmit={handleSubmit}>
            <Row className="g-3">
              {/* Product Code */}
              <Col md={6}>
                <AutocompleteInput
                  label="Product Code"
                  name="productCode"
                  value={formData.productCode}
                  onChange={async (e) => {
                    handleInputChange(e);
                    const code = e.target.value;
                    if (code && code.length >= 1) {
                      try {
                        const result = await productService.lookupByProductCode(
                          code
                        );
                        if (result && result.productName) {
                          setFormData((prev) => ({
                            ...prev,
                            productName: result.productName,
                          }));
                        }
                      } catch (_) {
                        // ignore lookup errors silently
                      }
                    }
                  }}
                  required
                  getSuggestions={productService.getProductCodeSuggestions}
                  placeholder="Enter product code"
                />
              </Col>

              {/* Product Name */}
              <Col md={6}>
                <AutocompleteInput
                  label="Product Name"
                  name="productName"
                  value={formData.productName}
                  onChange={async (e) => {
                    handleInputChange(e);
                    const name = e.target.value;
                    if (name && name.length >= 1) {
                      try {
                        const result = await productService.lookupByProductName(
                          name
                        );
                        // Fill product code if we have an exact name match
                        if (result && result.productCode) {
                          setFormData((prev) => ({
                            ...prev,
                            productCode: result.productCode,
                          }));
                        }
                      } catch (_) {
                        // ignore lookup errors silently
                      }
                    }
                  }}
                  required
                  getSuggestions={productService.getProductNameSuggestions}
                  placeholder="Enter product name"
                />
              </Col>

              {/* Packets */}
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Packets *</Form.Label>
                  <InputGroup>
                    <Button
                      variant="outline-secondary"
                      onClick={() => handlePacketsChange(-0.01)}
                      disabled={loading}
                    >
                      -
                    </Button>
                    <Form.Control
                      type="number"
                      step="0.01"
                      name="packets"
                      value={formData.packets}
                      onChange={handleInputChange}
                      min="0"
                      className="text-center"
                    />
                    <Button
                      variant="outline-secondary"
                      onClick={() => handlePacketsChange(0.01)}
                      disabled={loading}
                    >
                      +
                    </Button>
                  </InputGroup>
                </Form.Group>
              </Col>

              {/* Qty per Packet */}
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Qty per Packet *</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.01"
                    name="qtyPerPacket"
                    value={formData.qtyPerPacket}
                    onChange={handleInputChange}
                    min="0"
                    placeholder="Enter quantity per packet"
                  />
                </Form.Group>
              </Col>

              {/* Total Quantity */}
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Total Quantity</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.01"
                    name="quantity"
                    value={formData.quantity}
                    readOnly
                    className="bg-light"
                  />
                </Form.Group>
              </Col>

              {/* Unit */}
              <Col md={6}>
                <AutocompleteInput
                  label="Unit"
                  name="unit"
                  value={formData.unit}
                  onChange={handleInputChange}
                  required
                  getSuggestions={productService.getUnitSuggestions}
                  placeholder="Enter unit"
                />
              </Col>

              {/* Batch No */}
              <Col md={6}>
                <AutocompleteInput
                  label="Batch No."
                  name="batchNo"
                  value={formData.batchNo}
                  onChange={handleInputChange}
                  getSuggestions={productService.getBatchNoSuggestions}
                  placeholder="Enter batch number"
                />
              </Col>

              {/* GRN No */}
              <Col md={6}>
                <AutocompleteInput
                  label="GRN No."
                  name="grnNo"
                  value={formData.grnNo}
                  onChange={handleInputChange}
                  getSuggestions={productService.getGrnNoSuggestions}
                  placeholder="Enter GRN number"
                />
              </Col>

              {/* Sales Invoice No */}
              <Col md={6}>
                <AutocompleteInput
                  label="Sales Invoice No."
                  name="salesInvoiceNo"
                  value={formData.salesInvoiceNo}
                  onChange={handleInputChange}
                  getSuggestions={productService.getSalesInvoiceNoSuggestions}
                  placeholder="Enter sales invoice number"
                />
              </Col>

              {/* Material Type */}
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Material Type *</Form.Label>
                  <Form.Select
                    name="materialType"
                    value={formData.materialType}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Select material type</option>
                    <option value="RM">Raw Materials (RM)</option>
                    <option value="PM">Packing Materials (PM)</option>
                    <option value="FM">Finished Materials (FM)</option>
                  </Form.Select>
                </Form.Group>
              </Col>

              {/* Source */}
              <Col md={6}>
                <AutocompleteInput
                  label="Supplier/Client/Production Floor"
                  name="source"
                  value={formData.source}
                  onChange={handleInputChange}
                  required
                  getSuggestions={productService.getSourceSuggestions}
                  placeholder="Enter source"
                />
              </Col>

              {/* Date */}
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Date</Form.Label>
                  <Form.Control
                    type="datetime-local"
                    name="dateAdded"
                    value={formData.dateAdded}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </Col>

              {/* Submit Button */}
              <Col xs={12} className="text-center mt-4">
                <Button
                  type="submit"
                  variant="success"
                  size="lg"
                  disabled={loading}
                  className="px-5"
                >
                  {loading ? (
                    <>
                      <span
                        className="spinner-border spinner-border-sm me-2"
                        role="status"
                        aria-hidden="true"
                      ></span>
                      Saving...
                    </>
                  ) : (
                    <>
                      <BiSave /> Save Product
                    </>
                  )}
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default AddProduct;
