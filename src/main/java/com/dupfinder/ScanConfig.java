package com.dupfinder;
import java.nio.file.Path;


public record ScanConfig(Path directory, long minSize,
                         boolean dryRun, boolean interactiveDeleter,
                         Path jsonReport, Path htmlReport) {
}
