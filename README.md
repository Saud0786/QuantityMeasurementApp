# UC10 – Generic Quantity Class with Unit Interface

## 📌 Description

UC10 refactors UC1–UC9 into a single **generic and extensible measurement design** using:

- `IMeasurable` interface  
- `Quantity<U extends IMeasurable>` generic class  
- Unit enums implementing the interface (`LengthUnit`, `WeightUnit`)  

This eliminates duplication between `QuantityLength` and `QuantityWeight` and establishes a **scalable multi-category measurement architecture**.

---

## 🏗 Architecture

### 🔹 IMeasurable (Interface)

Defines the unit contract:

- `getConversionFactor()`  
- `convertToBaseUnit(double)`  
- `convertFromBaseUnit(double)`  
- `getUnitName()`  

---

### 🔹 Unit Enums

- `LengthUnit implements IMeasurable`  
- `WeightUnit implements IMeasurable`  

**Responsibilities:**
- Encapsulate conversion logic  
- Immutable and thread-safe  

---

### 🔹 Generic Quantity

**Fields:**
- `private final double value`  
- `private final U unit`  

**Behavior:**
- `equals()` using base-unit normalization  
- `convertTo(U targetUnit)`  
- `add(Quantity<U>)`  
- `add(Quantity<U>, U targetUnit)`  

**Characteristics:**
- Immutable value object  
- `hashCode()` consistent with `equals()`  

---

## ✅ Example Usage

### 📏 Length Equality

```java
new Quantity<>(1.0, LengthUnit.FEET)
        .equals(new Quantity<>(12.0, LengthUnit.INCHES)); // true

```
