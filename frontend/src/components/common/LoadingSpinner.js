import React from "react";
import { Spinner } from "react-bootstrap";

const LoadingSpinner = ({ size = "md", text = "Loading..." }) => {
  const getSizeClass = () => {
    switch (size) {
      case "sm":
        return "spinner-border-sm";
      case "lg":
        return "spinner-border-lg";
      default:
        return "";
    }
  };

  return (
    <div className="text-center mt-5">
      <Spinner animation="border" role="status" className={getSizeClass()}>
        <span className="visually-hidden">Loading...</span>
      </Spinner>
      {text && <p className="mt-2">{text}</p>}
    </div>
  );
};

export default LoadingSpinner;
