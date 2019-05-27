# AndroidNav

- Splash Screen is added.
- A registration page with first name, last name, email, password with validation.
- A login page with username, password, remember me.If Remember me is checked, the user will be in the session till log out.
- Add validation for username and password which are required 
- Firebase Authentication
- Once user logs in, redirect to an orders screen.
- Orders screen requirements:
- It has a log out button if remember me is checked at time of login.
- The user should be able to view all orders in a list view.
- Each order has Order Number, Order Due Date, Customer buyer name, Customer
Address, Customer Phone, OrderTotal, the gps location (city, state, country)
- Clicking on an order, user will be navigated to a screen where user can see edit and cancel order buttons
- A + Button will navigate to add order screen.
- Clicking on delete should ask user for confirmation and delete.
- Orders and users are stored in firebase 
- Tested on Android M.
