package com.processor.tax.service;

import static  com.processor.tax.constants.TaxConstants.FILE_NAME;
import static  com.processor.tax.constants.TaxConstants.SALES_TAXES;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.processor.tax.objects.Item;

@Service
public class ReceiptImpl implements Receipt {
	Double salesTax = 0.00;
	BigDecimal dec = null;

	@Override
	public String callLoader() {
		List<Item> itemList = new ArrayList<Item>();
		StringBuilder builder = new StringBuilder();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(FILE_NAME).getFile());
			InputStream inputStream = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String itemDetails;
			while ((itemDetails = reader.readLine()) != null) {
				Item item = new Item();
				item.setQuantity(getQuantity(itemDetails));
				item.setItemName(getDetail(itemDetails));
				item.setPrice(getPrice(itemDetails));
				item.setIsExempt(checkExempt(itemDetails));
				item.setIsImport(checkImported(itemDetails));
				float tax = checkItemtax(item);
				item.setAfterTax(taxCalcumation(tax, item));
				itemList.add(item);
				builder.append(itemDetails.split(" at")[0]).append(": ").append(item.getAfterTax()).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		BigDecimal bg = new BigDecimal(salesTax);
		dec = bg.setScale(2, RoundingMode.UP);
		builder.append(SALES_TAXES).append(dec).append("\n");
		double sum = itemList.stream().mapToDouble(Item::getAfterTax).sum();
		BigDecimal bgTax = new BigDecimal(sum);
		System.out.println("tax " + bgTax.toString());
		dec = bgTax.setScale(2, RoundingMode.DOWN);
		builder.append("Total: ").append(dec);
		//System.out.println(builder.toString());
		return builder.toString();
	}

	private Double taxCalcumation(float tax, Item item) {
		Double totalprice = (double) 0;
		if (tax == 0) {
			totalprice = item.getPrice() * item.getQuantity();
		} else {
			totalprice = item.getPrice() * item.getQuantity();
			Double price = totalprice * tax;
			salesTax = salesTax + price;
			totalprice = price + item.getPrice();
		}
		return (double) Math.round(totalprice * 100) / 100;
	}

	 float checkItemtax(Item item) {
		if (item.getIsImport() && !item.getIsExempt()) {
			return ((10.0f / 100) + (5.0f / 100));
		} else if (item.getIsImport() && item.getIsImport()) {
			return (5.0f / 100);
		} else if (!item.getIsImport() && !item.getIsExempt()) {
			return (10.0f / 100);
		}
		return 0;
	}

	private Boolean checkImported(String details) {
		Pattern importPattern = Pattern.compile("imported");
		return importPattern.matcher(details).find();
	}

	private int getQuantity(String details) {
		Pattern pattern = Pattern.compile("^[\\d+]+");
		Matcher match = pattern.matcher(details);
		match.find();
		return Integer.parseInt(match.group(0));
	}

	private String getDetail(String details) {
		Pattern p = Pattern.compile("(?!^\\d)[A-Za-z].+(?=\\sat\\s\\d+.\\d+$)");
		Matcher m = p.matcher(details);
		m.find();
		return m.group(0);
	}

	private Boolean checkExempt(String details) {
		Pattern exemptPattern = Pattern.compile("pills|chocolate|book|wine|xyz");
		return exemptPattern.matcher(details).find();

	}
	Double getPrice(String details) {
		Pattern pattern = Pattern.compile("\\d+.\\d+$");
		Matcher match = pattern.matcher(details);
		match.find();
		return Double.parseDouble(match.group(0));
	}

	

}
