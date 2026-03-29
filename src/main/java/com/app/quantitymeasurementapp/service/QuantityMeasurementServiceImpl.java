package com.app.quantitymeasurementapp.service;

import com.app.quantitymeasurementapp.exception.CategoryMismatchException;
import com.app.quantitymeasurementapp.exception.InvalidUnitException;
import com.app.quantitymeasurementapp.exception.InvalidUnitMeasurementException;
import com.app.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.app.quantitymeasurementapp.model.Quantity;
import com.app.quantitymeasurementapp.model.QuantityDTO;
import com.app.quantitymeasurementapp.model.QuantityMeasurementDTO;
import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.model.QuantityModel;
import com.app.quantitymeasurementapp.reposistory.QuantityMeasurementRepository;
import com.app.quantitymeasurementapp.unit.IMeasurable;
import com.app.quantitymeasurementapp.unit.LengthUnit;
import com.app.quantitymeasurementapp.unit.TemperatureUnit;
import com.app.quantitymeasurementapp.unit.VolumeUnit;
import com.app.quantitymeasurementapp.unit.WeightUnit;




import java.util.List;
import java.util.logging.*;

import org.springframework.stereotype.Service;
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService{
	// Logger for logging information and errors
	private static final Logger logger = Logger.getLogger(
	QuantityMeasurementServiceImpl.class.getName()
	);
	
	private QuantityMeasurementRepository repository;
	//constructor
	public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
		this.repository = repository;
	}
	
	private enum Operation {
		COMPARISON, CONVERSION, ADD, ADD_TO_TARGET, SUBTRACT, SUBTRACT_TO_TARGET, DIVIDE;
	}

	@Override
	public QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {		
		// 1. Map
		QuantityModel<IMeasurable> m1 = mapToModel(thisQuantityDTO);
		QuantityModel<IMeasurable> m2 = mapToModel(thatQuantityDTO);
		
		//validate
		validateModels(m1, m2);
		
		// 3. Create Domain Objects
	    Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
	    Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());
	    
	    double val1 = q1.convertTo(q1.getUnit());
	    double val2 = q2.convertTo(q2.getUnit());
	    
	    // 4. Use the equals method from Quantity.java
	    boolean isEqual = Double.compare(val1, val2)==0;
	    
	    // 5. Save to Repository
	    QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
    			thisQuantityDTO.value,
    			thisQuantityDTO.unit,
    			thisQuantityDTO.measurementType,
    			thatQuantityDTO.value,
    			thatQuantityDTO.unit,
    			thatQuantityDTO.measurementType,
    			Operation.COMPARISON.name(),
    			isEqual ? 1.0 : 0.0,
    			thisQuantityDTO.unit,
    			thisQuantityDTO.measurementType,
    			"null",
    			false,
    			"null"
    		);
	    repository.save(entity);
				
        return new QuantityMeasurementDTO().from(entity);
	}
	
	@Override
public QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {

    // 1. Map DTO → Model
    QuantityModel<IMeasurable> m1 = mapToModel(thisQuantityDTO);
    QuantityModel<IMeasurable> m2 = mapToModel(thatQuantityDTO);

    // 2. Validate (optional but recommended)
    if (m1 == null || m2 == null) {
        throw new QuantityMeasurementException("Invalid input for conversion");
    }

    if (m1.getUnit().getClass() != m2.getUnit().getClass()) {
        throw new CategoryMismatchException("Cannot convert between different measurement types");
    }

    // 3. Create domain object
    Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());

    // 4. Perform conversion
    double convertedValue = q1.convertTo(m2.getUnit());

    // 5. Save to DB (✅ FIX APPLIED HERE)
    QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
            thisQuantityDTO.getValue(),
            thisQuantityDTO.getUnit(),
            thisQuantityDTO.getMeasurementType(),

            thatQuantityDTO.getValue(),
            thatQuantityDTO.getUnit(),
            thatQuantityDTO.getMeasurementType(),

            Operation.CONVERSION.name(),

            convertedValue,

            thatQuantityDTO.getUnit(),              // ✅ FIXED (target unit)
            thatQuantityDTO.getMeasurementType(),   // ✅ FIXED (target type)

            "null",
            false,
            "null"
    );

    repository.save(entity);

    // 6. Return response
    return new QuantityMeasurementDTO().from(entity);
}

	@Override
	public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		return executeArithmetic(thatQuantityDTO, thisQuantityDTO, null, Operation.ADD);
	}

	@Override
	public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
		return executeArithmetic(thisQuantityDTO, thatQuantityDTO, targetUnitDTO, Operation.ADD_TO_TARGET);
	}

	@Override
	public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		return executeArithmetic(thatQuantityDTO, thisQuantityDTO, null, Operation.SUBTRACT);
	}

	@Override
	public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {;
		return executeArithmetic(thisQuantityDTO, thatQuantityDTO, targetUnitDTO, Operation.SUBTRACT_TO_TARGET);
	}

	@Override
	public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		return executeArithmetic(thisQuantityDTO, thatQuantityDTO, null, Operation.DIVIDE);
	}
	
	@Override
	public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
		return null;
	}

	@Override
	public List<QuantityMeasurementDTO> getMeasurementsByType(String type) {
		return null;
	}

	@Override
	public long getOperationCount(String operation) {
		return 0;
	}

	@Override
	public List<QuantityMeasurementDTO> getErrorHistory() {
		return null;
	}
	
	  /**
     * Helper to map DTO (Strings) to Model (Actual Unit Enums)
     */
    private QuantityModel<IMeasurable> mapToModel(QuantityDTO dto) {
    		if (dto == null) {
            throw new QuantityMeasurementException("Quantity data cannot be null");
        }
    		
        String type = dto.getMeasurementType();
        String unitName = dto.getUnit();
        IMeasurable unit;
        try {
	        	switch (type) {  
	            case "LengthUnit": unit = LengthUnit.valueOf(unitName); break;
	            case "VolumeUnit": unit = VolumeUnit.valueOf(unitName); break;
	            case "WeightUnit": unit = WeightUnit.valueOf(unitName); break;
	            case "TemperatureUnit": unit = TemperatureUnit.valueOf(unitName); break;
	            default: throw new InvalidUnitMeasurementException("Invalid Measurement Category: " + type);
	        }
        }
        catch(Exception e) {
        		throw new InvalidUnitException("Unit '" + unitName + "' is not valid for " + type);
        	}
        return new QuantityModel<>(dto.getValue(), unit);
    }
    

    /**
     * Validation logic as requested in the flow diagram
     */
    private void validateModels(QuantityModel<?> m1, QuantityModel<?> m2) {
        if (m1 == null || m2 == null) {
        		throw new QuantityMeasurementException("Measurement operands cannot be null"); 
        }
        
        if (m1.getUnit().getClass() != m2.getUnit().getClass()) {
        		throw new CategoryMismatchException("Incompatible types: " + m1.getUnit().getClass().getSimpleName() + " vs " + m2.getUnit().getClass().getSimpleName());        
        	}
        
        if (!Double.isFinite(m1.getValue()) || !Double.isFinite(m2.getValue())) {
        		throw new QuantityMeasurementException("Invalid numeric value provided");
        	}
    }
    
    /**
     * This will helper method reuse for all method 
     */
    private QuantityMeasurementDTO executeArithmetic(QuantityDTO d1, QuantityDTO d2, QuantityDTO target, Operation opType) {		
		// 1. Map
		QuantityModel<IMeasurable> m1 = mapToModel(d1);
		QuantityModel<IMeasurable> m2 = mapToModel(d2);
		QuantityModel<IMeasurable> mT = (target != null) ? mapToModel(target) : null;

		// 2. Validate
		validateModels(m1, m2);
		if (mT != null)
			validateModels(m1, mT);
		
		if (m1.getUnit() instanceof TemperatureUnit || m2.getUnit() instanceof TemperatureUnit) {
	        if (opType == Operation.DIVIDE || opType == Operation.ADD || opType == Operation.SUBTRACT 
	            || opType == Operation.ADD_TO_TARGET || opType == Operation.SUBTRACT_TO_TARGET) {
	            
	            throw new UnsupportedOperationException("Arithmetic operations not supported for TemperatureUnit");
	        }
	    }

		// 3. Domain Call (Quantity.java handles the actual Math)
		Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
		Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());

		Quantity<IMeasurable> result;
		if (opType.name().contains("ADD")) {
			result = (mT != null) ? q1.add(q2, mT.getUnit()) : q1.add(q2);
		}
		else if (opType.name().contains("SUBTRACT")) {
			result = (mT != null) ? q1.subtract(q2, mT.getUnit()) : q1.subtract(q2);
		}
		else{
			double value = q1.divide(q2);
			result = new Quantity<IMeasurable>(value, q1.getUnit());
		}

		// 4. Extract & Save (Persistence)
		double resVal = result.getValue();
		String resUnit = result.getUnit().toString();

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
				d1.getValue(), 
				d1.getUnit(),
				d1.getMeasurementType(), 
				d2.getValue(), 
				d2.getUnit(), 
				d2.getMeasurementType(), 
				opType.name(), 
				resVal, 
				resUnit,
				d1.getMeasurementType(),
				"null",
				false,
				"null");
		repository.save(entity);
		
		// 5. Return
		return new QuantityMeasurementDTO().from(entity);
	}
}