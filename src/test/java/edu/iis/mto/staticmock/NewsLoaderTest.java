package edu.iis.mto.staticmock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class})
public class NewsLoaderTest {

    @Before
    public void setup() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getReaderType())
               .thenReturn("dont matter");

        ConfigurationLoader configurationLoader = Mockito.mock(ConfigurationLoader.class);
        Mockito.when(configurationLoader.loadConfiguration())
               .thenReturn(configuration);

        mockStatic(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
    }

    @Test
    public void CheckIfPublicInfoIsAdded1TimeIfIncomingNewsHasOnePublicInfo() {
        IncomingNews incomingNews;
        NewsLoader newsLoader;

        incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("A", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("B", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("C", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("NONE1", SubsciptionType.NONE));

        NewsReader newsReader = Mockito.mock(NewsReader.class);
        Mockito.when(newsReader.read())
               .thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);

        when(NewsReaderFactory.getReader(any(String.class))).thenReturn(newsReader);

        newsLoader = new NewsLoader();

        PublishableNews publishableNews = Mockito.mock(PublishableNews.class);

        mockStatic(PublishableNews.class);
        when(PublishableNews.create()).thenReturn(publishableNews);

        newsLoader.loadNews();
        verify(publishableNews, times(1)).addPublicInfo(any(String.class));
        /*
         * verify(publishableNews, times(1)).addPublicInfo("NONE1"); verify(publishableNews,
         * times(1)).addPublicInfo("NONE2"); verify(publishableNews, times(2)).addPublicInfo(any(String.class));
         *
         * verify(publishableNews, times(1)).addForSubscription("A", SubsciptionType.A); verify(publishableNews,
         * times(1)).addForSubscription("B", SubsciptionType.B); verify(publishableNews,
         * times(1)).addForSubscription("C", SubsciptionType.C); verify(publishableNews,
         * times(3)).addForSubscription(any(String.class), any(SubsciptionType.class));
         */
    }

    @Test
    public void CheckIfInfoForSubscriptedIsAdded1TimeIfIncomingNewsHasOneInfoForSubscripted() {
        IncomingNews incomingNews;
        NewsLoader newsLoader;

        incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("C", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("NONE1", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("NONE1", SubsciptionType.NONE));

        NewsReader newsReader = Mockito.mock(NewsReader.class);
        Mockito.when(newsReader.read())
               .thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);

        when(NewsReaderFactory.getReader(any(String.class))).thenReturn(newsReader);

        newsLoader = new NewsLoader();

        PublishableNews publishableNews = Mockito.mock(PublishableNews.class);

        mockStatic(PublishableNews.class);
        when(PublishableNews.create()).thenReturn(publishableNews);

        newsLoader.loadNews();
        verify(publishableNews, times(1)).addForSubscription(any(String.class), any(SubsciptionType.class));
    }

}
