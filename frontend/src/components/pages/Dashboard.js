import React from "react";
import { Link } from "react-router-dom";

const Dashboard = () => {
  return (
    <div>
      <h1 className="mb-4">Dashboard</h1>

      <div className="row">
        <div className="col-md-4 mb-4">
          <div className="card text-center">
            <div className="card-body">
              <h5 className="card-title">
                <i className="fas fa-plus-circle text-primary fs-1"></i>
              </h5>
              <h6 className="card-subtitle mb-2 text-muted">Add Products</h6>
              <p className="card-text">Add new products to your inventory</p>
              <Link to="/products/add" className="btn btn-primary">
                Go to Add Product
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-4 mb-4">
          <div className="card text-center">
            <div className="card-body">
              <h5 className="card-title">
                <i className="fas fa-list text-success fs-1"></i>
              </h5>
              <h6 className="card-subtitle mb-2 text-muted">View Products</h6>
              <p className="card-text">
                View and manage your product inventory
              </p>
              <Link to="/products/list" className="btn btn-success">
                List Products
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-4 mb-4">
          <div className="card text-center">
            <div className="card-body">
              <h5 className="card-title">
                <i className="fas fa-chart-bar text-info fs-1"></i>
              </h5>
              <h6 className="card-subtitle mb-2 text-muted">Reports</h6>
              <p className="card-text">Generate reports and export data</p>
              <Link to="/reports" className="btn btn-info">
                View Reports
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-4 mb-4">
          <div className="card text-center">
            <div className="card-body">
              <h5 className="card-title">
                <i className="fas fa-file-upload text-info fs-1"></i>
              </h5>
              <h6 className="card-subtitle mb-2 text-muted">Upload Excel</h6>
              <p className="card-text">Upload excel file</p>
              <Link to="/upload" className="btn btn-info">
                Upload
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
