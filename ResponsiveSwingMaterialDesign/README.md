## JAVA RESPONSIVE SWING MATERIAL DESIGN FRAMEWORK

### BEAULTY AND RESPONSIVE SWING COMPONENTS

**COMPONENTS:**
- INPUT FIELD (PREDEFINED VALIDATIONS: EMAIL, PHONE, MONEY. OBS: YOU CAN CREATE YOUR OWN MASK/VALIDATION)
- PASSWORD FIELD
- TEXT AREA
- DATE FIELD
- DATE PICKER
- CHECK CIRCLE
- COMBOBOX
- IMAGE
- BUTTON

### Sign Up Form developed using this framework
https://www.youtube.com/watch?v=ZijJrgkgyUA&feature=youtu.be

[![SignUp Form](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Form.png)](https://www.youtube.com/watch?v=ZijJrgkgyUA&feature=youtu.be)

### Responsive Layout (Default 1280x800)
```java
// Create a responsive layout using ResponsiveLayout(frameWidth, frameHeight, columns, rows, jFrame)
ResponsiveLayout rl = new ResponsiveLayout(12, 12, jFrame);
```
![Responsive Layout](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_1.png)

```java
public static void main(String[] args) {
	// You can create your jframe normally, using this class you can do it faster.
	// build(String name, int x, int y, int width, int height, boolean moveable)
	JFrame frame = FrameUtility.build("Java Responsive Swing Material Design", 0, 0, 1280, 900, true);

	ResponsiveLayout rl = new ResponsiveLayout(12, 12, frame);

	rl.add(new InputField("Email de contato", rl).email().getComponent(), 4, 1);
	rl.add(new InputField("Telefone de contato", rl).phone().getComponent(), 4, 1);
	rl.add(new InputField("Valor em reais", rl).money().getComponent(), 4, 1);
	rl.addRow();
	rl.add(new PasswordField("Senha", rl).getComponent(), 6, 1);
	rl.add(new CheckCircle("Texto do Check", rl).getComponent(), 3, 1);
	rl.add(new CheckCircle("Texto do Check 2", rl).getComponent(), 3, 1);

	rl.add(new TextArea("Área de Texto", rl).getComponent(), 6, 4);
	rl.add(new TextArea("Área de Texto", rl).getComponent(), 6, 4);
	rl.add(new DateField("Data", rl).getComponent(), 6, 1);
	rl.add(new DateTimeField("Data e Hora", rl).getComponent(), 6, 1);

	frame.setVisible(true);
	frame.setLocationRelativeTo(null);
}
```

### Creating your own Theme

```java
// Create theme model using ThemeModel
ThemeModel theme = new ThemeModel("model_name", primaryColor, secondaryColor, errorColor, textColor, backgroundColor);

// Add your theme into the framework
ResponsiveSwingMaterialDesign.getThemesManager().addTheme(model);

// Set the current theme using setCurrentTheme(model) or setCurrentTheme("themeName")
ResponsiveSwingMaterialDesign.getThemesManager().setCurrentTheme("themeName");
```
### Applying themes

Available themes:
1. Light
2. Darcula
3. Dark

```java
// Set the current theme using setCurrentTheme(model) or setCurrentTheme("themeName")
ResponsiveSwingMaterialDesign.getThemesManager().setCurrentTheme("themeName");
```

### Skipping Rows
```java
// To Skip rows in responsive layout use addRow(). (There are some bugs to solve)
rl.addRow();
```

### Input Fields

```java
// There are some pre-validations that you can use on any input field you want.

inputField.email();
inputField.phone();
inputField..money();

// To create your own mask use setMask(regex)
setMask("(##) #####-####");

// To create your own validation rules use setRules(callback)

inputField.setRules(new Callback() {
	@Override
	public String done() {
		String value = inputField.getText();

		if (value.isEmpty())
			return "Type something here.";
			
		return null;
	}
});

```

### E-mail Field
```java
// Add InputField component into ResponsiveLayout using rl.add(component, columns, rows)
rl.add(new InputField("Email de contato", rl).email().getComponent(), 4, 1); // Using 4 columns and 1 row
```
![E-mail Field](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_4.png)

### Phone Field
```java
rl.add(new InputField("Telefone de contato", rl).phone().getComponent(), 4, 1); // Using 4 columns and 1 row
```
![Phone Field](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_5.png)

### Money Field (price)
```java
rl.add(new InputField("Valor em reais", rl).money().getComponent(), 4, 1); // Using 4 columns and 1 row
```
![Value Field](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_6.png)

### Password Field
```java
rl.add(new PasswordField("Senha", rl).getComponent(), 6, 1); // Using 6 columns and 1 row
```
![Password Field](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_7.png)

### Text Area
```java
rl.add(new TextArea("Área de Texto", rl).getComponent(), 6, 4);  // Using 6 columns and 4 row
```
![Text Area](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_8.png)

### Date Field
```java

DateField dateField = new DateField("Data", rl).getComponent();

// To set date use setDate(Date date)
dateField.setDate(new Date());

// To set date format use setDateFormat(String format)
setDateFormat("dd/MM/yyyy");

// To get the Date use getDate() and getDateText()
getDate();

rl.add(dateField, 6, 1);
```
![Date Field](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_10.png)

### Date and Time Field
```java
rl.add(new DateTimeField("Data e Hora", rl).getComponent(), 6, 1); // Using 6 columns and 1 row
```
![Date and Time Field](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_11.png)

### Components (Dracula theme)
![Components Dracula theme](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Darkmode.png)

### Date Picker
![Date Picker](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/Screenshot_9.png)

### Date Picker (Dracula theme)
![Date Picker](https://github.com/GustavoRolimSantos/Java/blob/master/ResponsiveSwingMaterialDesign/images/DatePicker%20DarkMode.png)

> COPYRIGHT BY GUSTAVO ROLIM DOS SANTOS 2020.
