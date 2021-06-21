package org.jbake.render;

import org.jbake.app.ContentStore;
import org.jbake.app.Renderer;
import org.jbake.app.configuration.DefaultJBakeConfiguration;
import org.jbake.app.configuration.JBakeConfiguration;
import org.jbake.template.RenderingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FeedRendererTest {

    @Test
    public void returnsZeroWhenConfigDoesNotRenderFeeds() throws RenderingException {
        FeedRenderer renderer = new FeedRenderer();

        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderFeed()).thenReturn(false);

        ContentStore contentStore = mock(ContentStore.class);

        Renderer mockRenderer = mock(Renderer.class);
        int renderResponse = renderer.render(mockRenderer, contentStore, configuration);

        assertThat(renderResponse).isEqualTo(0);
    }

    @Test
    public void doesNotRenderWhenConfigDoesNotRenderFeeds() throws Exception {
        FeedRenderer renderer = new FeedRenderer();

        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderFeed()).thenReturn(false);

        ContentStore contentStore = mock(ContentStore.class);
        Renderer mockRenderer = mock(Renderer.class);

        renderer.render(mockRenderer, contentStore, configuration);

        verify(mockRenderer, never()).renderFeed(anyString());
    }

    @Test
    public void returnsOneWhenConfigRendersFeeds() throws RenderingException {
        FeedRenderer renderer = new FeedRenderer();

        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderFeed()).thenReturn(true);

        ContentStore contentStore = mock(ContentStore.class);

        Renderer mockRenderer = mock(Renderer.class);

        int renderResponse = renderer.render(mockRenderer, contentStore, configuration);

        assertThat(renderResponse).isEqualTo(1);
    }

    @Test
    public void doesRenderWhenConfigDoesRenderFeeds() throws Exception {
        FeedRenderer renderer = new FeedRenderer();
        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderFeed()).thenReturn(true);
        when(configuration.getFeedFileName()).thenReturn("mockfeedfile.xml");

        ContentStore contentStore = mock(ContentStore.class);
        Renderer mockRenderer = mock(Renderer.class);

        renderer.render(mockRenderer, contentStore, configuration);

        verify(mockRenderer, times(1)).renderFeed(anyString());
    }

    @Test
    public void propogatesRenderingException() throws Exception {
        assertThrows(RenderingException.class, () -> {
            FeedRenderer renderer = new FeedRenderer();

            JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
            when(configuration.getRenderFeed()).thenReturn(true);
            when(configuration.getFeedFileName()).thenReturn("mockfeedfile.xml");

            ContentStore contentStore = mock(ContentStore.class);
            Renderer mockRenderer = mock(Renderer.class);

            doThrow(new Exception()).when(mockRenderer).renderFeed(anyString());

            renderer.render(mockRenderer, contentStore, configuration);

            verify(mockRenderer, never()).renderFeed("random string");
        });
    }

}


