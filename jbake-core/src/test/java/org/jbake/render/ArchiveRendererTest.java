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

public class ArchiveRendererTest {

    @Test
    public void returnsZeroWhenConfigDoesNotRenderArchives() throws RenderingException {
        ArchiveRenderer renderer = new ArchiveRenderer();

        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderArchive()).thenReturn(false);

        ContentStore contentStore = mock(ContentStore.class);

        Renderer mockRenderer = mock(Renderer.class);
        int renderResponse = renderer.render(mockRenderer, contentStore, configuration);

        assertThat(renderResponse).isEqualTo(0);
    }

    @Test
    public void doesNotRenderWhenConfigDoesNotRenderArchives() throws Exception {
        ArchiveRenderer renderer = new ArchiveRenderer();

        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderArchive()).thenReturn(false);

        ContentStore contentStore = mock(ContentStore.class);
        Renderer mockRenderer = mock(Renderer.class);

        renderer.render(mockRenderer, contentStore, configuration);

        verify(mockRenderer, never()).renderArchive(anyString());
    }

    @Test
    public void returnsOneWhenConfigRendersArchives() throws RenderingException {
        ArchiveRenderer renderer = new ArchiveRenderer();

        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderArchive()).thenReturn(true);

        ContentStore contentStore = mock(ContentStore.class);

        Renderer mockRenderer = mock(Renderer.class);

        int renderResponse = renderer.render(mockRenderer, contentStore, configuration);

        assertThat(renderResponse).isEqualTo(1);
    }

    @Test
    public void doesRenderWhenConfigDoesRenderArchives() throws Exception {
        ArchiveRenderer renderer = new ArchiveRenderer();

        JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
        when(configuration.getRenderArchive()).thenReturn(true);
        when(configuration.getArchiveFileName()).thenReturn("mockarchive.html");

        ContentStore contentStore = mock(ContentStore.class);
        Renderer mockRenderer = mock(Renderer.class);

        renderer.render(mockRenderer, contentStore, configuration);

        verify(mockRenderer, times(1)).renderArchive(anyString());
    }

    @Test
    public void propogatesRenderingException() throws Exception {
        assertThrows(RenderingException.class, () -> {
            ArchiveRenderer renderer = new ArchiveRenderer();

            JBakeConfiguration configuration = mock(DefaultJBakeConfiguration.class);
            when(configuration.getRenderArchive()).thenReturn(true);
            when(configuration.getArchiveFileName()).thenReturn("mockarchive.html");

            ContentStore contentStore = mock(ContentStore.class);
            Renderer mockRenderer = mock(Renderer.class);

            doThrow(new Exception()).when(mockRenderer).renderArchive(anyString());

            renderer.render(mockRenderer, contentStore, configuration);

            verify(mockRenderer, never()).renderArchive("random string");
        });
    }

}


