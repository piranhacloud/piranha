# Release process

The release process described below uses the 'current' branch. For patch releases see the [Patch releases](#patch-releases) section below

## On the 12th of the month

1. Create release-X.Y.Z branch from current
1. Run Servlet TCK
1. Run Pages TCK
1. Run EL TCK
1. Run Jakarta EE samples
1. If any of the above runs fail
    1. Checkout the release X.Y.Z branch
    1. Apply whatever fixes are needed (keep git diff copies)
    1. Commit and push them to release-X.Y.Z branch
    1. Redo steps #2 - #5
1. On the current branch update the POMs to point to the next SNAPSHOT version

## On the 13th of the month

1. Create DRAFT release notes
1. Trigger release using release-X.Y.Z as branch to release from
1. If release build fails
    1. Remove the vX.Y.Z tag
    1. Checkout the release-X.Y.Z branch
    1. Apply whatever changes are needed (keep git diff copy)
    1. Commit and push them to release-X.Y.Z
    1. Retrigger the release
1. Wait for release bits to show up on Maven central
1. Open DRAFT release notes
1. Set correct tag for DRAFT release notes
1. Make sure “Create a discussion for this release” is checked
1. Make sure Category is set to General 
1. Publish release notes
1. On the piranha_cloud Twitter announce that Piranha X.Y.Z has been released and make sure to thank our contributors.
1. Delete the release-X.Y.Z branch
1. Close the milestone on GitHub

## Patch releases

If you are creating a patch release against a branch please follow all the
steps above and replace current with the branch you are patching against where 
appropriate (without waiting a day in between).
