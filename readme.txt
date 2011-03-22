Author: Chris Tan
Purpose: Performing assertions on the Ping Identity website using HttpUnit.

Special thanks to this blog for showing me how to ignore the 3rd party errors which prevented HttpUnit from working:
http://technicalmumbojumbo.wordpress.com/2009/07/12/setting-anonymousstealth-login-to-web-applications-the-httpunit-way/

In order to add additional tests for an additional domain, first create a folder for the tests under the ..\HttpUnit\testscripts folder e.g. C:\Users\user\workspace\HttpUnit\testscripts\www.painlesstesting.com\