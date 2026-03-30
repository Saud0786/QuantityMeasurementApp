package com.app.quantitymeasurementapp.model;

public enum OperationType {
  ADD,
  SUBTRACT,
  MULTIPTY,
  DIVIDE,
  COMPARE,
  CONVERT;
	

	public String getDisplayName() {
		return this.name().toLowerCase();
	}
}
