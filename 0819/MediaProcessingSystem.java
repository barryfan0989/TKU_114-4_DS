abstract class MediaFile {
    private final String fileName;
    private final long fileSize; // bytes

    MediaFile(String fileName, long fileSize) {
        this.fileName = (fileName == null || fileName.trim().isEmpty()) ? "Unknown_File" : fileName.trim();
        this.fileSize = Math.max(0, fileSize);
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    abstract void getFileInfo();
}

interface Playable {
    void play();
}

class ImageFile extends MediaFile implements Compressible {
    private final String resolution;

    ImageFile(String fileName, long fileSize, String resolution) {
        super(fileName, fileSize);
        this.resolution = (resolution == null || resolution.trim().isEmpty()) ? "Unknown Resolution" : resolution.trim();
    }

    @Override
    void getFileInfo() {
        System.out.printf("Image File: %s | Size: %.2f KB | Resolution: %s%n", 
            getFileName(), getFileSize() / 1024.0, resolution);
    }

    @Override
    public void compress() {
        System.out.println("-> [Compressing Image] Reducing resolution quality for " + getFileName() + " to save storage.");
    }
}

class AudioFile extends MediaFile implements Playable {
    private final int duration; // seconds

    AudioFile(String fileName, long fileSize, int duration) {
        super(fileName, fileSize);
        this.duration = Math.max(0, duration);
    }

    @Override
    void getFileInfo() {
        System.out.printf("Audio File: %s | Size: %.2f MB | Duration: %d min %d sec%n", 
            getFileName(), getFileSize() / (1024.0 * 1024.0), duration / 60, duration % 60);
    }

    @Override
    public void play() {
        System.out.println("-> [Playing Audio] Decoding MP3/AAC stream for: " + getFileName());
    }
}

class VideoFile extends MediaFile implements Playable, Compressible {
    private final String resolution;
    private final int duration; // seconds

    VideoFile(String fileName, long fileSize, String resolution, int duration) {
        super(fileName, fileSize);
        this.resolution = (resolution == null || resolution.trim().isEmpty()) ? "Unknown Resolution" : resolution.trim();
        this.duration = Math.max(0, duration);
    }

    @Override
    void getFileInfo() {
        System.out.printf("Video File: %s | Size: %.2f MB | Resolution: %s | Duration: %d min %d sec%n", 
            getFileName(), getFileSize() / (1024.0 * 1024.0), resolution, duration / 60, duration % 60);
    }

    @Override
    public void play() {
        System.out.println("-> [Playing Video] Hardware decoding MP4/MKV stream: " + getFileName() + " at " + resolution);
    }

    @Override
    public void compress() {
        System.out.println("-> [Compressing Video] Transcoding " + getFileName() + " using H.265 compression format.");
    }
}

public class MediaProcessingSystem {
    public static void main(String[] args) {
        MediaFile[] library = {
            new ImageFile("vacation_photo.png", 2048576, "3840x2160"),
            new AudioFile("podcast_ep12.mp3", 45097152, 2705),
            new VideoFile("lecture_video.mp4", 512984064, "1920x1080", 7210)
        };

        System.out.println("=== Media Processing System ===");

        for (MediaFile file : library) {
            System.out.println("\n-------------------------------------------");
            file.getFileInfo();

            // Check and execute interface actions
            if (file instanceof Playable playable) {
                playable.play();
            }
            if (file instanceof Compressible compressible) {
                compressible.compress();
            }
        }
    }
}
