package co.com.bancolombia.batch.aws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.WritableResource;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class S3StreamWriter<T> implements ItemWriter<T> {

    private WritableResource writableResource;
    private LineAggregator<T> lineAggregator;
    private String lineSeparator;

    public S3StreamWriter(WritableResource writableResource) {
        this.writableResource = writableResource;
    }

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        try (OutputStream outputStream = this.writableResource.getOutputStream()) {
            var line = new StringBuilder();
            for (T item : chunk) {
                line.append(lineAggregator.aggregate(item)).append(lineSeparator);
            }
            outputStream.write(line.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
