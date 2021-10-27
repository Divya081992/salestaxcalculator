package com.processor.tax.service;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.processor.tax.objects.Item;

public class ReceiptImplTest {
	
	 ReceiptImpl receipt = new ReceiptImpl();
	
	private Item item;
	String details = "1 book at 12.49";
	String expectedBill = "1 book: 12.49\n"
			+ "1 music CD: 16.49\n"
			+ "1 chocolate bar: 0.85\n"
			+ "Sales Taxes: 1.50\n"
			+ "Total: 29.83";
	@BeforeEach
	    public void initialize() {
		 	item  = new Item();
	        item.setItemName("book");
	        item.setPrice(12.49);
	        item.setQuantity(1);

	    }
	 
	 @Test
	 public void checkItemName()  {
		 Double price = receipt.getPrice("1 book at 12.49");
	       assertEquals(price,item.getPrice());
	    }
	 
	@SuppressWarnings(value = { "" })
	@Test
	 public void checkItemTestType()  {
		 float type = receipt.checkItemtax(item);
	      assertEquals(0.1f,type, 0.00);
	    }
	
	@Test
	 public void checkbill()  {
		 String bill = receipt.callLoader();
	     assertEquals(expectedBill, bill);
	    }
	 
	 
	 

}
