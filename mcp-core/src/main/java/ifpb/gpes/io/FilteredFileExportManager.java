/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.gpes.io;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.filter.FilterByMethod;
import ifpb.gpes.filter.FilterClassType;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author juan
 */
public class FilteredFileExportManager extends FileExportManager{
    
    public FilteredFileExportManager(String name) {
        super(name);
    }
    
    public static ExportManager name(String file){
       return new FilteredFileExportManager(file);
    }
    
    @Override
    public void export(List<Call> elements) {
        StringBuilder calls = new StringBuilder();
        calls.append("\n\n----Candidatos----\n\n");
        List<Call> collect = elements.stream().filter(new FilterClassType("java.util.Collection")).collect(Collectors.toList());
        calls.append(collect.stream()
                .map(Call::callGraph)
                .collect(Collectors.joining("\n")));
        calls.append("\n\n----Culpados----\n\n");
        calls.append(collect.stream().filter(new FilterByMethod())
                .map(Call::callGraph)
                .collect(Collectors.joining("\n")));
        super.write(calls.toString());
    }
}
