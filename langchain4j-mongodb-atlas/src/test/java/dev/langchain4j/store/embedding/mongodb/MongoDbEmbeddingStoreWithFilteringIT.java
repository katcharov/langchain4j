package dev.langchain4j.store.embedding.mongodb;

import com.mongodb.client.MongoClient;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreWithFilteringIT;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static dev.langchain4j.store.embedding.mongodb.TestHelper.*;

public class MongoDbEmbeddingStoreWithFilteringIT extends EmbeddingStoreWithFilteringIT {

    /**
     * If container startup timeout (because atlas cli need to download mongodb binaries, which may take a few minutes),
     * the alternative way is running `docker compose up -d` in `src/test/resources`
     */
    public static class ContainerIT extends MongoDbEmbeddingStoreWithRemovalIT {
        static MongoDBAtlasContainer mongodb = new MongoDBAtlasContainer();

        @BeforeAll
        static void start() {
            TestHelper.assertDoContainerTests();
            mongodb.start();
        }

        @AfterAll
        static void stop() {
            mongodb.stop();
        }

        @Override
        MongoClient createClient() {
            return createClientFromContainer(mongodb);
        }
    }

    private TestHelper helper = new TestHelper(createClient()).initialize();

    MongoClient createClient() {
        return createClientFromEnv();
    }

    @Override
    protected EmbeddingStore<TextSegment> embeddingStore() {
        return helper.getEmbeddingStore();
    }

    @Override
    protected EmbeddingModel embeddingModel() {
        return EMBEDDING_MODEL;
    }

    @AfterEach
    void afterEach() {
        helper.afterTests();
    }
}
