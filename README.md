## Commands:  

1. Command: NEWPRODUCT productName  
2. Command: PURCHASE productName amount price date  
3. Command: DEMAND productName amount price date  
4. Command: SALESREPORT productName date  
5. Command: INFO 
	
### Rules:

- NEWPRODUCT before PURCHASE/DEMAND  
- PURCHASE before DEMAND  
- DEMAND before SALESREPORT  
- Amount > 0 AND Price > 0  
- Date <date> in format DD.MM.YYYY  

### Possible results:

- For commands NEWPRODUCT / PURCHASE / DEMAND:  

		if (command syntax is correct)
			OK
		else
			ERROR
- For command SALESREPORT:

			int Profit
		if (command syntax is correct)
			Profit
		else
			ERROR
- For command INFO:

		if (command syntax is correct)  
		{
			Commands:
				1) Command: NEWPRODUCT <productName>
				2) Command: PURCHASE <productName> <amount> <price> <date>
				3) Command: DEMAND <productName> <amount> <price> <date>
				4) Command: SALESREPORT <productName> <date>
				5) Command: INFO
			Rules: 
				- NEWPRODUCT before PURCHASE/DEMAND
				- PURCHASE before DEMAND
				- DEMAND before SALESREPORT
				- Amount > 0 AND Price > 0
				- Date <date> in format DD.MM.YYYY
		}
		else
			Try again! It's simple: INFO
			
---------------------------------------------------
### Start main:
		class CommandManager_Invoker
---------------------------------------------------
### log4j info:
```html
	<!-- For Tomcat -->
	<param name="file" value="${catalina.home}/logs/Moysklad.log" />
```
### Java
jdk-12.0.1