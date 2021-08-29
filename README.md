# Library

Used to manage the books I want to read/have already read.

There are 3 languages - Russian, English, and Spanish; 2 book types - Finished Books and Books To Read; and a special Additional Dates entity used when a book is read more than once.

## 

Books I want to read have the "_found_" property which represents the date when a book has been added to the list.

In addition to the "_found_" property, Finished Books have the "_start_" and "_end_" properties which represent the date when I started reading a book and the date I finished it.

## 

Features:
<li>saving tables to/creating from JSON files</li>
<li>additional dates for Finished Books in case I read a book in different periods of time or read it again</li>