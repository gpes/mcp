package br.edu.ifpb.gpes.export;

import ifpb.gpes.ExportManager;

public enum ExportStrategy {

    BROKE {

    },
    JCF {

    };

    public abstract ExportManager exportFactory(String outputDir);
}
