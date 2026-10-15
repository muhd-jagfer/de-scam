package com.descam.backend.url;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlAnalysisService
{
  public static final Pattern URL_PATTERN = Pattern.compile("https?://[^\\s]+", Pattern.CASE_INSENSITIVE);

  public String extractUrl(String message)
{
if (message == null || message.isBlank())
{
return null;
}

Matcher matcher = URL_PATTERN.matcher(message);

if (matcher.find())
{
    String url = matcher.group();

    while (!url.isEmpty() &&
        ".,!?;:".indexOf(url.charAt(url.length() - 1)) >= 0)
{
    url = url.substring(0, url.length() - 1);
}
    return url.isEmpty() ? null : url;
}

return null;

}

  public String extractDomain(String url)
  {
    try 
    {
      URI uri = URI.create(url);
      return uri.getHost();
    } catch (Exception e) {
      return null;
    }
  }

  public boolean isHttps (String url)
  {
    try{
      URI uri = URI.create(url);
      return "https".equalsIgnoreCase(uri.getScheme());
    } catch (Exception e) {
      return false;
    }
  }
}