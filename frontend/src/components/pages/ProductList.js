import React, { useState, useEffect } from "react";
import {
  Container,
  Card,
  Button,
  Form,
  InputGroup,
  Table,
  Dropdown,
  Badge,
  Spinner,
} from "react-bootstrap";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import {
  BiPlusCircle,
  BiSearch,
  BiFilter,
  BiTrash,
  BiSortAlt2,
} from "react-icons/bi";
import { productService } from "../../services/api";
import { Row, Col } from "react-bootstrap";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterType, setFilterType] = useState("all");
  const [sortField, setSortField] = useState("createdAt");
  const [sortDirection, setSortDirection] = useState("desc");

  useEffect(() => {
    fetchProducts();
  }, []);

  useEffect(() => {
    filterAndSortProducts();
  }, [products, searchTerm, filterType, sortField, sortDirection]);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const response = await productService.getAllProducts();
      if (response.success) {
        setProducts(response.data);
      } else {
        toast.error(response.message || "Error loading products");
      }
    } catch (error) {
      toast.error(error.message || "Error loading products");
    } finally {
      setLoading(false);
    }
  };

  const filterAndSortProducts = () => {
    let filtered = [...products];

    // Apply search filter
    if (searchTerm) {
      filtered = filtered.filter(
        (product) =>
          product.productCode
            .toLowerCase()
            .includes(searchTerm.toLowerCase()) ||
          product.productName
            .toLowerCase()
            .includes(searchTerm.toLowerCase()) ||
          product.batchNo?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          product.source.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Apply material type filter
    if (filterType !== "all") {
      filtered = filtered.filter(
        (product) => product.materialType === filterType
      );
    }

    // Apply sorting
    filtered.sort((a, b) => {
      let aValue = a[sortField];
      let bValue = b[sortField];

      // Handle date fields
      if (sortField === "createdAt" || sortField === "dateAdded") {
        aValue = new Date(aValue);
        bValue = new Date(bValue);
      }

      // Handle string comparison
      if (typeof aValue === "string" && typeof bValue === "string") {
        aValue = aValue.toLowerCase();
        bValue = bValue.toLowerCase();
      }

      if (sortDirection === "asc") {
        return aValue > bValue ? 1 : -1;
      } else {
        return aValue < bValue ? 1 : -1;
      }
    });

    setFilteredProducts(filtered);
  };

  const handleSearch = () => {
    // Search is handled by useEffect
  };

  const handleSort = (field) => {
    if (sortField === field) {
      setSortDirection(sortDirection === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortDirection("asc");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this product?")) {
      try {
        const response = await productService.deleteProduct(id);
        if (response.success) {
          toast.success("Product deleted successfully!");
          fetchProducts();
        } else {
          toast.error(response.message || "Error deleting product");
        }
      } catch (error) {
        toast.error(error.message || "Error deleting product");
      }
    }
  };

  const getMaterialTypeBadge = (materialType) => {
    const variants = {
      RM: "primary",
      PM: "warning",
      FM: "success",
    };

    const labels = {
      RM: "Raw Material",
      PM: "Packing Material",
      FM: "Finished Material",
    };

    return (
      <Badge bg={variants[materialType]} className="material-badge">
        {labels[materialType]}
      </Badge>
    );
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString();
  };

  if (loading) {
    return (
      <Container className="text-center mt-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
        <p className="mt-2">Loading products...</p>
      </Container>
    );
  }

  return (
    <Container>
      <Card className="shadow-sm">
        <Card.Header className="bg-white d-flex justify-content-between align-items-center">
          <h2 className="mb-0">Product Inventory</h2>
          <div>
            <Link to="/products/add" className="text-decoration-none">
              <Button variant="primary" size="sm">
                <BiPlusCircle /> Add Product
              </Button>
            </Link>
          </div>
        </Card.Header>
        <Card.Body>
          {/* Search and Filter Controls */}
          <Row className="mb-3">
            <Col md={6}>
              <InputGroup>
                <Form.Control
                  type="text"
                  placeholder="Search products..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  onKeyPress={(e) => e.key === "Enter" && handleSearch()}
                />
                <Button variant="outline-secondary" onClick={handleSearch}>
                  <BiSearch />
                </Button>
              </InputGroup>
            </Col>
            <Col md={6} className="text-end">
              <Dropdown className="d-inline-block">
                <Dropdown.Toggle
                  variant="outline-secondary"
                  id="filterDropdown"
                >
                  <BiFilter /> Filter
                </Dropdown.Toggle>
                <Dropdown.Menu align="end">
                  <Dropdown.Item
                    onClick={() => setFilterType("all")}
                    active={filterType === "all"}
                  >
                    All Products
                  </Dropdown.Item>
                  <Dropdown.Divider />
                  <Dropdown.Item
                    onClick={() => setFilterType("RM")}
                    active={filterType === "RM"}
                  >
                    Raw Materials
                  </Dropdown.Item>
                  <Dropdown.Item
                    onClick={() => setFilterType("PM")}
                    active={filterType === "PM"}
                  >
                    Packing Materials
                  </Dropdown.Item>
                  <Dropdown.Item
                    onClick={() => setFilterType("FM")}
                    active={filterType === "FM"}
                  >
                    Finished Materials
                  </Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </Col>
          </Row>

          {/* Products Table */}
          <div className="table-responsive">
            <Table striped hover>
              <thead className="table-light">
                <tr>
                  <th
                    className="sortable-header"
                    onClick={() => handleSort("productCode")}
                  >
                    Code <BiSortAlt2 className="sort-icon" />
                  </th>
                  <th
                    className="sortable-header"
                    onClick={() => handleSort("productName")}
                  >
                    Name <BiSortAlt2 className="sort-icon" />
                  </th>
                  <th>Quantity</th>
                  <th>Unit</th>
                  <th>Batch No.</th>
                  <th>Type</th>
                  <th>Source</th>
                  <th>Date Added</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredProducts.length > 0 ? (
                  filteredProducts.map((product) => (
                    <tr key={product.id}>
                      <td>{product.productCode}</td>
                      <td>{product.productName}</td>
                      <td>{product.quantity}</td>
                      <td>{product.unit}</td>
                      <td>{product.batchNo || "N/A"}</td>
                      <td>{getMaterialTypeBadge(product.materialType)}</td>
                      <td>{product.source}</td>
                      <td>{formatDate(product.dateAdded)}</td>
                      <td>
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => handleDelete(product.id)}
                        >
                          <BiTrash /> Delete
                        </Button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="9" className="text-center py-4">
                      No products found.{" "}
                      <Link to="/products/add">Add your first product</Link>
                    </td>
                  </tr>
                )}
              </tbody>
            </Table>
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default ProductList;
