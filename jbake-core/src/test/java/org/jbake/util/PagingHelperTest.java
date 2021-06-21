package org.jbake.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PagingHelperTest {
    @Test
    public void getNumberOfPages() throws Exception {
        int expected = 3;
        int total = 5;
        int perPage = 2;

        PagingHelper helper = new PagingHelper(total,perPage);

        Assertions.assertEquals( expected, helper.getNumberOfPages() );
    }

    @Test
    public void shouldReturnRootIndexPage() throws Exception {
        PagingHelper helper = new PagingHelper(5,2);

        String previousFileName = helper.getPreviousFileName(2);

        assertThat("", is( previousFileName) );
    }

    @Test
    public void shouldReturnPreviousFileName() throws Exception {
        PagingHelper helper = new PagingHelper(5,2);

        String previousFileName = helper.getPreviousFileName(3);

        assertThat("2/", is( previousFileName) );
    }

    @Test
    public void shouldReturnNullIfNoPreviousPageAvailable() throws Exception {
        PagingHelper helper = new PagingHelper(5,2);

        String previousFileName = helper.getPreviousFileName(1);

        Assertions.assertNull( previousFileName );
    }

    @Test
    public void shouldReturnNullIfNextPageNotAvailable() throws Exception {
        PagingHelper helper = new PagingHelper(5,2);

        String nextFileName = helper.getNextFileName(3);

        Assertions.assertNull( nextFileName );
    }

    @Test
    public void shouldReturnNextFileName() throws Exception {
        PagingHelper helper = new PagingHelper(5,2);

        String nextFileName = helper.getNextFileName(2);

        assertThat("3/", is( nextFileName) );
    }
}