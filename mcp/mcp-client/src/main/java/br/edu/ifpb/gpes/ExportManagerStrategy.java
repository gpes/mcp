package br.edu.ifpb.gpes;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;

import java.util.List;

public enum ExportManagerStrategy implements ExportManager {
    BROKE {
        @Override
        public void export(List<Call> elements) {

        }
    }
}
