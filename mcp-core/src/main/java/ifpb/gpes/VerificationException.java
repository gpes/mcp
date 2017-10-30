/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.gpes;

/**
 *
 * @author juan
 */
public class VerificationException extends RuntimeException{
    public VerificationException(Exception e) {
        super("Ocorreu uma falha na verificação", e);
    }
}
