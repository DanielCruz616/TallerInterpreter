package patroninterpreter.demo;

import patroninterpreter.expressions.TerminalExpression;
import patroninterpreter.expressions.OrExpression;
import patroninterpreter.expressions.Expression;
import patroninterpreter.expressions.AndExpression;
import patroninterpreter.expressions.NotExpression; 

public class InterpreterDemo {
    
    public static Expression getAdminOrManagerExpression() {
        Expression admin = new TerminalExpression("ADMIN");
        Expression manager = new TerminalExpression("MANAGER");
        return new OrExpression(admin, manager);
    }

    public static Expression getAdminAndUserExpression() {
        Expression admin = new TerminalExpression("ADMIN");
        Expression user = new TerminalExpression("USER");
        return new AndExpression(admin, user);
    }

    public static Expression getClausulaValidaExpression() {

        // Sujeto
        Expression sujeto = new OrExpression(
            new TerminalExpression("arrendatario"),
            new TerminalExpression("inquilino")
        );

        // Acción
        Expression accion = new OrExpression(
            new TerminalExpression("pagará"),
            new TerminalExpression("abonará")
        );

        // Objeto
        Expression objeto = new OrExpression(
            new TerminalExpression("renta"),
            new TerminalExpression("canon")
        );

        // Tiempo
        Expression tiempo = new OrExpression(
            new TerminalExpression("mensual"),
            new TerminalExpression("anticipado")
        );

        // Palabra prohibida
        Expression prohibido = new NotExpression(
            new TerminalExpression("subarrendar")
        );

        // Árbol completo
        return new AndExpression(
            new AndExpression(
                new AndExpression(sujeto, accion),
                new AndExpression(objeto, tiempo)
            ),
            prohibido
        );
    }

    public static void main(String[] args) {

        Expression clausulaValida = getClausulaValidaExpression();

        System.out.println("\n--- VALIDACION DE CLAUSULAS ---");
        String clausula = "El arrendatario pagará el canon mensual";
        System.out.println("Clausula valida? " + clausulaValida.interpret(clausula));

        String clausula2 = "El inquilino abonará la renta anticipado subarrendar";
        System.out.println("Clausula valida? " + clausulaValida.interpret(clausula2));

        Expression isPrivileged = getAdminOrManagerExpression();
        Expression isStrict = getAdminAndUserExpression();

        System.out.println("\n--- PRUEBAS DE VALIDACION DE PERMISOS ---");
        
        String context1 = "ADMIN";
        System.out.println("El contexto '" + context1 + "' tiene acceso privilegiado? " 
            + isPrivileged.interpret(context1));

        String context2 = "USER";
        System.out.println("El contexto '" + context2 + "' tiene acceso privilegiado? " 
            + isPrivileged.interpret(context2));

        String context3 = "ADMIN, USER";
        System.out.println("El contexto '" + context3 + "' cumple el rol estricto (Admin + User)? " 
            + isStrict.interpret(context3));
    }
}