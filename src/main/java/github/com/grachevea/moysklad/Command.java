/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.util.List;

/**
 *
 * @author GrachevEA
 */
public interface Command {
    void execute(List<String> strList);
}
