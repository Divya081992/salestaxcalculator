package com.processor.tax.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
public class Item {
	private @Getter @Setter int quantity;
	private @Getter @Setter String itemName;
	private @Getter @Setter Double price;
	private @Getter @Setter Boolean isImport = false;
	private @Getter @Setter Boolean isExempt = false;
	private @Getter @Setter Double afterTax;

}
