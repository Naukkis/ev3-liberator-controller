package application;

public class Model {

	public int Palauta(String operator)
    {
        switch (operator) {
            case "Manual":
                return 1;
            case "Demo":
                return 2;
            case "Forward":
                return 3;
            case "Left":
                return 4;
            case "Start":
            	return 5;
            case "Stop":
            	return 6;
            case "Right":
            	return 7;
            case "Backward":
            	return 8;
            case "Gui Control":
            	return 9;
            case "Keyboard Control:":
            	return 10;
        }
		return 0;
    }
}
