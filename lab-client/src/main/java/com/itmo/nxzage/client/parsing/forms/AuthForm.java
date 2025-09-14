package com.itmo.nxzage.client.parsing.forms;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.itmo.nxzage.client.auth.User;
import com.itmo.nxzage.client.io.InputManager;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.client.parsing.fields.Field;
import com.itmo.nxzage.client.parsing.fields.StringField;

public class AuthForm implements Form<Map<String, Object>> {
    private boolean interactive;
    private Map<String, Object> result;

    private <T> T fillField(Field<T> field, InputManager in, OutputHandler out)
            throws ParseException {
        if (interactive) {
            out.printPrompt(field.getPrompt());
        }
        try {
            return field.parse(in.nextLine());
        } catch (ParseException exception) {
            if (interactive) {
                out.printError(exception.getMessage() + "\n");
                return fillField(field, in, out);
            }
            throw exception;
        }
    }

    @Override
    public Map<String, Object> fill(InputManager in, OutputHandler out) throws ParseException {
        interactive = in.isInteractive();
        String username = fillField(new StringField("Enter username: "), in, out);
        String password = fillField(new StringField("Enter password: "), in, out);
        result = new HashMap<>();
        result.put("username", username);
        result.put("user_password", password);
        return result;
    }
    
}
