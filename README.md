# Jesktop Applications

Historical work I did on SourceForge while unemployed after 9/11: An attempt to make a desktop app using J2SE 1.4 
and Swing. This was a bunch of applications forked from elsewhere in OpenSourceLand and made to use an [API](https://github.com/javadesktop/Jesktop-API) to allow an desktop app experience. Years active 2002 - 2004.

See also the [Jesktop Implementation](https://github.com/javadesktop/Jesktop-Implementation) repo.

# Conversion from SourceForge CVS.

I tried a few things that didn't work, but this is what I doing: Two stages using Subversion temporarily:

```
rsync -a a.cvs.sourceforge.net::cvsroot/jesktop jesktop
mkdir Svn_conv
cd Svn_conv/
svnadmin create foo
cvs2svn --existing-svnrepos -s foo /path/to/jesktop
git svn --ignore-paths CVSROOT --stdlayout --no-metadata clone file:///path/to/jesktop/Svn_conv/foo bar 
cd bar/
git remote add origin git@github.com:javadesktop/Jesktop-API.git
git push -u origin master
```
