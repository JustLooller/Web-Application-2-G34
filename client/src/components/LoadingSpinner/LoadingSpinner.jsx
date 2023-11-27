import React from "react";
import Spinner from "react-bootstrap/Spinner";
import "./LoadingSpinner.css";

const LoadingSpinner = () => {
  return (
    <div className="loading-spinner-container">
      <Spinner
        animation="border"
        role="status"
        className="custom-spinner"
      ></Spinner>
    </div>
  );
};

export default LoadingSpinner;
