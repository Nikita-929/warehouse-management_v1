import React, { useState, useEffect, useRef } from "react";
import { Form } from "react-bootstrap";

const AutocompleteInput = ({
  label,
  name,
  value,
  onChange,
  onBlur,
  required = false,
  getSuggestions,
  placeholder = "",
  type = "text",
  step,
  min,
  readOnly = false,
  ...props
}) => {
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [loading, setLoading] = useState(false);
  const inputRef = useRef(null);
  const suggestionsRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        suggestionsRef.current &&
        !suggestionsRef.current.contains(event.target) &&
        inputRef.current &&
        !inputRef.current.contains(event.target)
      ) {
        setShowSuggestions(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const handleInputChange = async (e) => {
    const inputValue = e.target.value;
    onChange(e);

    if (inputValue.length < 1) {
      setSuggestions([]);
      setShowSuggestions(false);
      return;
    }

    setLoading(true);
    try {
      const response = await getSuggestions(inputValue);
      if (response.success) {
        setSuggestions(response.data);
        setShowSuggestions(true);
      }
    } catch (error) {
      console.error("Error fetching suggestions:", error);
      setSuggestions([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSuggestionClick = (suggestion) => {
    // Update the controlled value via onChange
    onChange({
      target: {
        name,
        value: suggestion,
      },
    });
    // Also reflect immediately in the input to avoid any race with blur
    if (inputRef.current) {
      inputRef.current.value = suggestion;
    }
    setShowSuggestions(false);
  };

  const handleInputBlur = (e) => {
    setTimeout(() => {
      setShowSuggestions(false);
    }, 200);
    if (onBlur) {
      onBlur(e);
    }
  };

  return (
    <div className="autocomplete-container">
      <Form.Group className="mb-3">
        <Form.Label>
          {label} {required && <span className="text-danger">*</span>}
        </Form.Label>
        <Form.Control
          ref={inputRef}
          type={type}
          name={name}
          value={value}
          onChange={handleInputChange}
          onBlur={handleInputBlur}
          placeholder={placeholder}
          required={required}
          step={step}
          min={min}
          readOnly={readOnly}
          autoComplete="off"
          {...props}
        />
        {loading && (
          <div className="position-absolute top-50 end-0 translate-middle-y me-3">
            <div
              className="spinner-border spinner-border-sm text-primary"
              role="status"
            >
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        )}
      </Form.Group>

      {showSuggestions && suggestions.length > 0 && (
        <div ref={suggestionsRef} className="autocomplete-suggestions">
          {suggestions.map((suggestion, index) => (
            <div
              key={index}
              className="autocomplete-item"
              onMouseDown={(e) => {
                e.preventDefault();
                handleSuggestionClick(suggestion);
              }}
            >
              {suggestion}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AutocompleteInput;
