# Release process

The release process described below uses the 'current' branch. For patch 
releases see the [Patch releases](#patch-releases) section below

## On the 12th of the month

1. Create release-X.Y.Z branch from current
1. Wait for the build on the release-X.Y.Z branch to complete
1. If the build was not successful
    1. Checkout the release-X.Y.Z branch
    1. Apply whatever fixes are needed
    1. Commit and push them to release-X.Y.Z branch
    1. Redo steps #2 and #3
1. On the current branch update the POMs to point to the next SNAPSHOT version

## On the 13th of the month

1. Trigger release using release-X.Y.Z as branch to release from
1. If release build fails
    1. Remove the vX.Y.Z tag
    1. Checkout the release-X.Y.Z branch
    1. Apply whatever fixes are needed
    1. Commit and push them to release-X.Y.Z
    1. Retrigger the release
1. Create release notes
1. Set correct tag for release notes
1. Make sure “Create a discussion for this release” is checked
1. Make sure Category is set to General 
1. Publish release notes
1. Apply fixes from release-X.Y.Z branch to the current branch with a corresponding issue (if any).
1. Delete the release-X.Y.Z branch
1. Close the milestone on GitHub
1. Wait for release bits to show up on Maven Central
1. On the piranha_cloud Twitter announce that Piranha X.Y.Z has been released and make sure to thank our contributors.

## Patch releases

If you are creating a patch release against a branch please follow all the
steps above and replace current with the branch you are patching against where 
appropriate (without waiting a day in between).
