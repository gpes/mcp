package br.edu.ifpb.gpes.export;

import ifpb.gpes.ExportManager;
import ifpb.gpes.graph.io.BrokeExportManager;
import ifpb.gpes.io.PrintOutManager;
import ifpb.gpes.jcf.io.CategoryExportManager;

public enum ExportStrategy {

    BROKE {
        @Override
        public ExportManager exportFactory(String outputDir) {
            return new BrokeExportManager(outputDir);
        }
    },
    JCF {
        @Override
        public ExportManager exportFactory(String outputDir) {
            return new CategoryExportManager(outputDir);
        }
    },
    PRINT {
        @Override
        public ExportManager exportFactory(String outputDir) {
            return new PrintOutManager(null);
        }
    };

    public abstract ExportManager exportFactory(String outputDir);
}
