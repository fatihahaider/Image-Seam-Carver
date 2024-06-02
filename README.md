# Image-Seam-Carver
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. Unlike standard content-agnostic resizing techniques (such as cropping and scaling), seam carving preserves the most interest features (aspect ratio, set of objects present, etc.) of the image.

/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */

to find the vertical seam I use dynamic programming to update a double  array
of values that correlate to the energy values of a given pictures. when vertical
seam is called, it creates a new double array called energy and fills all of the
elements with their respective values. then the second double array is made and
all values are set to inifinty and then the first array is set to their own
energies because that's techinically the "distance" to that pixel. After that,
i iterate through each row checking the upper left upper middle and upper right
pixel of the current one and add their energies together and compare that
value to the value already stored in the distTo array. the min distance will
be saved. at the same time, for which every pixel is the min total energy, that
x value index is saved in another double array. at the end i fidn the pixel with
the min total energy and then use the index of the array to backtrack the
distTo array and find the min seam.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

it might not work for a picture that is the same color all through out meaning
every pixel would have the same energy and so it would always "carve" the first
row and horizontal which isnt that helpful

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
250             0.170
500             0.219           1.288         0.365
1000            0.395           1.803         0.850
2000            0.687           1.739         0.798
4000            1.175           1.710         0.773
8000            2.579           2.194         1.133


(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
250             0.175
500             0.274           1.565          0.646
1000            0.411           1.500          0.589
2000            0.641           1.599          0.677
4000            1.219           1.901          0.926
8000            2.599           2.132          1.092


/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~  1.624 x 10^-7 * W * H
       _______________________________________




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

my width and height functions/variables were not working and thus gavve me
outrageous out of bounds errors.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
