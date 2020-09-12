package logic.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import comm.constants.AuthLevel.Role;
import slecon.ToolBox;
import slecon.ToolBox.AVersion;

public class PageTreeExpression extends SimpleConfiguareExpression {
    private static final HashMap<String, Method> readMethodCache = new HashMap<>();
    private Version    version;
    private List<Role> roles;
    private final String expr;

    public PageTreeExpression(final String expr) {
        super(expr);
        this.expr = expr;
    }
    
    public String getExpression() {
        return expr;
    }
    
    public Boolean evaluate(Version version, Role[] roles) {
        this.version = version;
        this.roles = Arrays.asList(roles);
        return super.evaluate();
    }

    protected boolean toValue(String name, String op, String version) {
        if (this.version != null) {
            try {
                Method methodRead = readMethodCache.get(name);
                if (methodRead == null) {
                    PropertyDescriptor pd = new PropertyDescriptor(name, Version.class, name, null);
                    methodRead = pd.getReadMethod();
                    readMethodCache.put(name, methodRead);
                }

                if (methodRead != null) {
                    Object reValue = methodRead.invoke(this.version);
                    if (reValue instanceof String) {
                        AVersion a = ToolBox.parserVersion((String) reValue);
                        AVersion b = ToolBox.parserVersion((String) version);
                        int result = a.compareTo(b);
                        if (">=".equals(op) && result >= 0 || ">".equals(op) && result > 0 || "=".equals(op) && result == 0 || "<=".equals(op)
                                && result <= 0 || ">".equals(op) && result < 0)
                            return true;
                    }
                }
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    protected boolean toValue(String constant) {
        if("debugMode".equalsIgnoreCase(constant)) {
            return ToolBox.isDebugMode();
        }
        if (roles!=null) {
            Role role = Role.valueOf(constant.toUpperCase());
            return roles.contains(role);
        }
        throw new UnsupportedOperationException();
    }
}
