package br.edu.ifpb.gpes.export;

import ifpb.gpes.ExportManager;
import ifpb.gpes.graph.io.BrokeExportManager;
import ifpb.gpes.io.PrintOutManager;
import ifpb.gpes.jcf.io.CategoryInterfaceExportManager;
import ifpb.gpes.jcf.io.CategoryMethodExportManager;

public enum ExportStrategy {

    BROKE {
        @Override
        public ExportManager exportFactory(String outputDir) {
            return new BrokeExportManager(outputDir);
        }
    },
    CATEGORYINTERFACE {
        @Override
        public ExportManager exportFactory(String outputDir) {
            return new CategoryInterfaceExportManager(outputDir);
        }
    },
    CATEGORYMETHOD {
        @Override
        public ExportManager exportFactory(String outputDir) {
            return new CategoryMethodExportManager(outputDir);
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
